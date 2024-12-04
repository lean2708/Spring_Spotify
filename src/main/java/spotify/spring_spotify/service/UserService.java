package spotify.spring_spotify.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.entity.Playlist;
import spotify.spring_spotify.entity.Role;
import spotify.spring_spotify.entity.User;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PlaylistMapper;
import spotify.spring_spotify.mapper.RoleMapper;
import spotify.spring_spotify.mapper.UserMapper;
import spotify.spring_spotify.repository.PlaylistRepository;
import spotify.spring_spotify.repository.RoleRepository;
import spotify.spring_spotify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(UserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new SpotifyException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // playlist
        if(request.getCreatedPlaylists() != null){
            List<Playlist> playlistList = playlistRepository
                    .findAllByTitleIn(new ArrayList<>(request.getCreatedPlaylists()))
                    .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));
            for(Playlist playlist : playlistList){
                playlist.setCreator(user.getName());
            }
           user.setCreatedPlaylists(new HashSet<>(playlistList));
        }
        else{
            user.setCreatedPlaylists(new HashSet<>());
        }

        if(request.getRoles() != null){
            List<Role> roleList = roleRepository
                    .findAllByNameIn(new ArrayList<>(request.getRoles()));
            user.setRoles(new HashSet<>(roleList));
        }
        else{
            user.setRoles(new HashSet<>());
        }

        return convertUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse fetchById(long id){
        User userDB = userRepository.findById(id).
                orElseThrow(() -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));
        return convertUserResponse(userDB);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> fetchAllUser(){
        List<User> listUser = userRepository.findAll();
        return convertListUserResponse(listUser);
    }

    public List<PlaylistBasic> fetchSavedPlaylists(){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        List<Playlist> savedList = playlistRepository.findAllByIdIn(user.getSavedPlaylistId())
                .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));

        List<PlaylistBasic> playlistBasics = new ArrayList<>();
        for(Playlist playlist : savedList){
            PlaylistBasic basic = playlistMapper.toPlaylistBasic(playlist);
            playlistBasics.add(basic);
        }
        return playlistBasics;
    }

    public UserResponse update(long id, UserRequest request){
        User userDB = userRepository.findById(id).
                orElseThrow(() -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        if (userDB.getEmail() != null && !userDB.getEmail().equals(request.getEmail())) {
            throw new SpotifyException(ErrorCode.EMAIL_IMMUTABLE);
        }
        User user = userMapper.updateUser(userDB,request);

        // playlist
        if(request.getCreatedPlaylists() != null){
            List<Playlist> playlistList = playlistRepository
                    .findAllByTitleIn(new ArrayList<>(request.getCreatedPlaylists()))
                    .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));
            for(Playlist playlist : playlistList){
                playlist.setCreator(user.getName());
            }
            user.setCreatedPlaylists(new HashSet<>(playlistList));
        }
        else{
            user.setCreatedPlaylists(new HashSet<>());
        }

        if(request.getRoles() != null){
            List<Role> roleList = roleRepository
                    .findAllByNameIn(new ArrayList<>(request.getRoles()));
            user.setRoles(new HashSet<>(roleList));
        }
        else{
            user.setRoles(new HashSet<>());
        }

        return convertUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(long id){
        User userDB = userRepository.findById(id).
                orElseThrow(() -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        userRepository.delete(userDB);
    }

    public UserResponse convertUserResponse(User user){
        UserResponse response = userMapper.toUserResponse(user);

        Set<PlaylistBasic> playlistBasicSet = user.getCreatedPlaylists()
                .stream().map(playlistMapper::toPlaylistBasic).collect(Collectors.toSet());
        response.setCreatedPlaylists(playlistBasicSet);

        Set<RoleResponse> roleResponses = user.getRoles().stream()
                .map(roleMapper::toRoleResponse).collect(Collectors.toSet());
        response.setRoles(roleResponses);
        return response;
    }

    public PlaylistBasic createSavedPlaylists(long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new SpotifyException(ErrorCode.PLAYLIST_NOT_EXISTED));

        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        List<Long> idList = user.getSavedPlaylistId();
        idList.add(playlist.getId());
        user.setSavedPlaylistId(idList);

        userRepository.save(user);

        return playlistMapper.toPlaylistBasic(playlist);
    }

    public List<UserResponse> convertListUserResponse(List<User> userList){
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User user : userList){
            UserResponse response = convertUserResponse(user);
            userResponseList.add(response);
        }
        return userResponseList;
    }


}
