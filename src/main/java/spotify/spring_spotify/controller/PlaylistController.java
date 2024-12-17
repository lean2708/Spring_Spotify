package spotify.spring_spotify.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
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
@RequestMapping("/v1")
public class PlaylistController {
    private final PlaylistService playlistService;
    @PostMapping(value = "/playlist",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PlaylistResponse> create(@Valid @ModelAttribute PlaylistRequest request,
                                                @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException, SAXException {
        return ApiResponse.<PlaylistResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(playlistService.create(request, multipartFile))
                .message("Create Playlist")
                .build();
    }

    @GetMapping(value = "/playlist/{id}")
    public ApiResponse<PlaylistResponse> fetchById(@PathVariable long id){
        return ApiResponse.<PlaylistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.fetchById(id))
                .message("Fetch Playlist By Id")
                .build();
    }
    @GetMapping("/playlists")
    public ApiResponse<PageResponse<PlaylistResponse>> fetchAll( @RequestParam(defaultValue = "1") int pageNo,
                                                              @RequestParam(defaultValue = "3") int pageSize,
                                                              @RequestParam(defaultValue = "asc") String titleSortOrder){
        return ApiResponse.<PageResponse<PlaylistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.fetchAllPlaylists(pageNo, pageSize, titleSortOrder))
                .message("Fetch All Playlist With Pagination")
                .build();
    }
    @GetMapping("/playlists/search")
    public ApiResponse<PageResponse<PlaylistResponse>> searchPlaylists(@RequestParam("title") @NotBlank String name,
                                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                                 @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<PlaylistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.searchPlaylists(name, pageNo, pageSize))
                .message("Search Playlists By Name With Pagination")
                .build();
    }
    @PutMapping(value = "/playlist/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PlaylistResponse> update(@PathVariable long id,@Valid @ModelAttribute PlaylistRequest request,
                                                @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException,  SAXException {
        return ApiResponse.<PlaylistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.update(id, request,multipartFile))
                .message("Update Playlist")
                .build();
    }
    @DeleteMapping("/playlist/{id}")
    private ApiResponse<Void> delete(@PathVariable long id){
        playlistService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Playlist")
                .build();
    }
    @DeleteMapping("/playlist/{playlistId}/song/{songId}")
    private ApiResponse<Void> removeSong(@PathVariable long playlistId, @PathVariable long songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Song removed from Playlist")
                .build();
    }
    @GetMapping("/playlists/sorted-by-viewer")
    public ApiResponse<PageResponse<PlaylistResponse>> fetchAllSortedByViewer(
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "desc") String viewerSortOrder) {

        return ApiResponse.<PageResponse<PlaylistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(playlistService.fetchAllPlaylistSortedByViewer(pageNo, pageSize, viewerSortOrder))
                .message("Fetched All Playlist Sorted by Viewer Count")
                .build();
    }
}
