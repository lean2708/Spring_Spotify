package spotify.spring_spotify.service;

import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.entity.Playlist;
import spotify.spring_spotify.entity.User;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.mapper.PlaylistMapper;
import spotify.spring_spotify.mapper.UserMapper;
import spotify.spring_spotify.repository.PlaylistRepository;
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

    public UserResponse create(UserRequest request){
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

        return response;
    }
    public UserResponse fetchById(long id){
        User userDB = userRepository.findById(id).
                orElseThrow(() -> new SpotifyException("User không tồn tại"));
        UserResponse response = userMapper.toUserResponse(userDB);

        Set<PlaylistBasic> playlistBasicSet = userDB.getPlaylists()
                .stream().map(playlistMapper::toPlaylistBasic).collect(Collectors.toSet());
        response.setPlaylists(playlistBasicSet);

        return response;
    }
    public List<UserResponse> fetchAll(){
        List<User> listUser = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User user : listUser){
            UserResponse response = userMapper.toUserResponse(user);
            Set<PlaylistBasic> playlistBasicSet = user.getPlaylists()
                    .stream().map(playlistMapper::toPlaylistBasic).collect(Collectors.toSet());
            response.setPlaylists(playlistBasicSet);
            userResponseList.add(response);
        }
        return userResponseList;
    }

    public UserResponse update(long id, UserRequest request){
        User userDB = userRepository.findById(id).
                orElseThrow(() -> new SpotifyException("User không tồn tại"));

        if (userDB.getEmail() != null && !userDB.getEmail().equals(request.getEmail())) {
            throw new SpotifyException("Email không thể bị thay đổi");
        }
        User user = userMapper.updateUser(userDB,request);

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

        return response;
    }
    public void deleteUser(long id){
        User userDB = userRepository.findById(id).
                orElseThrow(() -> new SpotifyException("User không tồn tại"));

        userRepository.delete(userDB);
    }


}
