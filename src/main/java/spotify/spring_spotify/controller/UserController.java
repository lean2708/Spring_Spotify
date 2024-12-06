package spotify.spring_spotify.controller;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import spotify.spring_spotify.dto.basic.PlaylistBasic;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/user")
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(userService.create(request))
                .message("Create User with Role")
                .build();
    }
    @GetMapping("/user/{id}")
    public ApiResponse<UserResponse> fetchById(@PathVariable long id){

        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(userService.fetchById(id))
                .message("Get User By Id")
                .build();
    }
    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> fetchAll(){
        return ApiResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(userService.fetchAllUser())
                .message("Fetch All User")
                .build();
    }

    @PutMapping(value = "/user/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponse> update(@PathVariable long id,@Valid @ModelAttribute UserRequest request,
                                            @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(userService.update(id, request, multipartFile))
                .message("Update User")
                .build();
    }
    @DeleteMapping("/user/{id}")
    public ApiResponse<Void> delete(@PathVariable long id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete User")
                .build();
    }
    @PostMapping("/user/saved-playlist/{idPlaylist}")
    public ApiResponse<PlaylistBasic> createSavedPlaylists(@PathVariable long id){
        return ApiResponse.<PlaylistBasic>builder()
                .code(HttpStatus.OK.value())
                .result(userService.createSavedPlaylists(id))
                .message("Save Playlist With User")
                .build();
    }
    @GetMapping("/user/saved-playlists")
    public ApiResponse<List<PlaylistBasic>> fetchSavedPlaylists(){
        return ApiResponse.<List<PlaylistBasic>>builder()
                .code(HttpStatus.OK.value())
                .result(userService.fetchSavedPlaylists())
                .message("Fetch Playlist Save with User")
                .build();
    }

}
