package spotify.spring_spotify.controller;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import spotify.spring_spotify.dto.request.PlaylistRequest;
import spotify.spring_spotify.dto.response.*;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PlaylistResponse> create(@Valid @ModelAttribute PlaylistRequest request,
                                                @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException {
        return ApiResponse.<PlaylistResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(playlistService.create(request, multipartFile))
                .message("Create Playlist")
                .build();
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<PlaylistResponse> fetchById(@PathVariable long id){
        return ApiResponse.<PlaylistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.fetchById(id))
                .message("Fetch Playlist By Id")
                .build();
    }
    @GetMapping()
    public ApiResponse<List<PlaylistResponse>> fetchAll(){
        return ApiResponse.<List<PlaylistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.fetchAllPlaylist())
                .message("Fetch All Playlist")
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<PageResponse<PlaylistResponse>> searchPlaylists(@RequestParam("title") String name,
                                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                                 @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<PlaylistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.searchPlaylists(name, pageNo, pageSize))
                .message("Search Playlists By Name With Pagination")
                .build();
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PlaylistResponse> update(@PathVariable long id,@Valid @ModelAttribute PlaylistRequest request,
                                                @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException {
        return ApiResponse.<PlaylistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.update(id, request,multipartFile))
                .message("Update Playlist")
                .build();
    }
    @DeleteMapping("/{id}")
    private ApiResponse<Void> delete(@PathVariable long id){
        playlistService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Playlist")
                .build();
    }
}
