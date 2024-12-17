package spotify.spring_spotify.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import spotify.spring_spotify.constant.RoleEnum;
import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.request.*;
import spotify.spring_spotify.dto.response.*;
import spotify.spring_spotify.entity.*;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PlaylistMapper;
import spotify.spring_spotify.mapper.RoleMapper;
import spotify.spring_spotify.mapper.UserMapper;
import spotify.spring_spotify.repository.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PlaylistMapper playlistMapper;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final InvalidatedRepository invalidatedRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final UserService userService;

    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;
    @Value("${jwt.validity-in-days}")
    protected long tokenExpiration;


    public AuthResponse login(AuthRequest request) throws JOSEException {
        User userDB = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder(10);
        boolean isauthenticated = passwordEncoder.matches(request.getPassword(), userDB.getPassword());

        if(!isauthenticated){
            throw new SpotifyException(ErrorCode.INVALID_PASSWORD);
        }

        String token = generateToken(userDB);

        return AuthResponse.builder()
                .token(token)
                .authenticaed(true)
                .build();
    }

    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new SpotifyException(ErrorCode.EMAIL_EXISTED);
        }

        User user = userMapper.toUserByRegister(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("USER").orElseThrow(
                () -> new SpotifyException(ErrorCode.ROLE_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        if(user.getRoles() != null){
            roles.addAll(user.getRoles());
        }
        roles.add(userRole);
        user.setRoles(roles);

        UserResponse response = userMapper.toUserResponse(userRepository.save(user));

        Set<RoleResponse> roleResponses = user.getRoles().stream()
                .map(roleMapper::toRoleResponse).collect(Collectors.toSet());
        response.setRoles(roleResponses);

        emailService.sendUserEmailWithRegister(user);

        return response;
    }


    public String generateToken(User user) throws JOSEException {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(user.getName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(tokenExpiration, ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));

        return jwsObject.serialize();
    }

    public IntrospectResponse introspect(TokenRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        boolean inValid = true;
        try {
            verifyToken(token);
        } catch (SpotifyException e){
            inValid = false;
        }
        return IntrospectResponse.builder()
                .valid(inValid)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SINGER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isverifed = signedJWT.verify(verifier);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        if(!isverifed && !expityTime.after(new Date()) || jwtId == null){
            throw new SpotifyException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedRepository.existsById(jwtId)){
            throw new SpotifyException(ErrorCode.UNAUTHORIZED);
        }
        return signedJWT;
    }

    public String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }

    public void logout(TokenRequest request) throws ParseException, JOSEException {
        SignedJWT signToken = verifyToken(request.getToken());
        String wti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(wti)
                .expiryTime(expiryTime)
                .build();

        invalidatedRepository.save(invalidatedToken);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        UserResponse response = userService.convertUserResponse(user);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public StatsResponse getStatsCounts() {

        long totalUsers = userRepository.count();

        long totalAlbums = albumRepository.count();

        long totalArtists = artistRepository.count();

        long totalSongs = songRepository.count();

        long totalPlaylists = playlistRepository.count();

        return StatsResponse.builder()
                .totalUsers(totalUsers)
                .totalAlbums(totalAlbums)
                .totalArtists(totalArtists)
                .totalPlaylists(totalPlaylists)
                .totalSongs(totalSongs)
                .build();
    }

}
