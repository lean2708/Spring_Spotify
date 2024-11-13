package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.request.AuthRequest;
import spotify.spring_spotify.dto.request.TokenRequest;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.AuthResponse;
import spotify.spring_spotify.dto.response.IntrospectResponse;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.entity.InvalidatedToken;
import spotify.spring_spotify.entity.Playlist;
import spotify.spring_spotify.entity.User;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PlaylistMapper;
import spotify.spring_spotify.mapper.UserMapper;
import spotify.spring_spotify.repository.InvalidatedRepository;
import spotify.spring_spotify.repository.PlaylistRepository;
import spotify.spring_spotify.repository.UserRepository;
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
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final EmailService emailService;
    private final InvalidatedRepository invalidatedRepository;

    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;
    @Value("${jwt.validity-in-days}")
    protected long tokenExpiration;

    public UserResponse register(UserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new SpotifyException("Email đã tồn tại");
        }

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // playlist
        if(request.getPlaylists() != null){
            List<Playlist> playlistList = playlistRepository
                    .findAllByTitleIn(new ArrayList<>(request.getPlaylists()));
            user.setPlaylists(new HashSet<>(playlistList));
        }
        else{
            user.setPlaylists(new HashSet<>());
        }

        UserResponse response = userMapper.toUserResponse(userRepository.save(user));
        Set<PlaylistBasic> playlistBasicSet = user.getPlaylists()
                .stream().map(playlistMapper::toPlaylistBasic).collect(Collectors.toSet());
        response.setPlaylists(playlistBasicSet);

        this.sendUserEmail(user);

        return response;
    }
    public void sendUserEmail(User user){
        if(user != null){
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getName());
            emailService.sendEmail(user.getEmail(),
                    "Chúc mừng! Tài khoản Spotify của bạn đã được đăng kí thành công",
                    model,
                    "index"
            );
        }
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

    public AuthResponse authenticated(AuthRequest request) throws JOSEException {
        User userDB = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new SpotifyException("User không tồn tại"));

        PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder(10);
        boolean isauthenticated = passwordEncoder.matches(request.getPassword(), userDB.getPassword());
        if(!isauthenticated){
            throw new SpotifyException("Bạn chưa được phân quyền");
        }
        String token = generateToken(request.getEmail());

        return AuthResponse.builder()
                .token(token)
                .authenticaed(true)
                .build();
    }
    public String generateToken(String email) throws JOSEException {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer(email)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(tokenExpiration, ChronoUnit.DAYS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);


        jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));

        return jwsObject.serialize();
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
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SINGER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isverifed = signedJWT.verify(verifier);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        if(!isverifed && !expityTime.after(new Date()) || jwtId == null){
            throw new SpotifyException("Token không hợp lệ");
        }

        if(invalidatedRepository.existsById(jwtId)){
            throw new SpotifyException("Token không hợp lệ");
        }
        return signedJWT;
    }
}
