package spotify.spring_spotify.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import spotify.spring_spotify.dto.request.SongRequest;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.service.SongService;
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
public class SongController {
    private final SongService songService;
    @PostMapping(value = "/song",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ApiResponse<SongResponse> create(@Valid  @ModelAttribute SongRequest songRequest,
             @RequestParam(value = "image", required = false) MultipartFile image,
                                             @RequestParam(value = "fileSong", required = false) MultipartFile fileSong)
            throws FileException, IOException, SAXException {
        return ApiResponse.<SongResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(songService.create(songRequest, image, fileSong))
                .message("Create Song")
                .build();
    }
    @GetMapping("song/{id}")
    public ApiResponse<SongResponse> fetchById(@PathVariable long id){
        return ApiResponse.<SongResponse>builder()
                .code(HttpStatus.OK.value())
                .result(songService.fetchById(id))
                .message("Fetch Song By Id")
                .build();
    }
    @GetMapping("/songs")
    public ApiResponse<PageResponse<SongResponse>> fetchAll(  @RequestParam(defaultValue = "1") int pageNo,
                                                      @RequestParam(defaultValue = "3") int pageSize,
                                                      @RequestParam(defaultValue = "asc") String nameSortOrder){
        return ApiResponse.<PageResponse<SongResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(songService.fetchAllSong(pageNo, pageSize, nameSortOrder))
                .message("Fetch All Song With Pagination")
                .build();
    }
    @GetMapping("/songs/search")
    public ApiResponse<PageResponse<SongResponse>> searchSongs(@RequestParam @NotBlank String name,
                                                                       @RequestParam(defaultValue = "1") int pageNo,
                                                                       @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<SongResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(songService.searchSongs(name, pageNo, pageSize))
                .message("Search Songs By Name With Pagination")
                .build();
    }
    @PutMapping(value = "/song/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SongResponse> update(@PathVariable long id, @Valid @ModelAttribute SongRequest request,
                                            @RequestParam(value = "image", required = false) MultipartFile image,
                                            @RequestParam(value = "fileSong", required = false) MultipartFile fileSong) throws FileException, IOException,  SAXException {

        return ApiResponse.<SongResponse>builder()
                .code(HttpStatus.OK.value())
                .result(songService.update(id,request,image, fileSong))
                .message("Update Song")
                .build();
    }
    @DeleteMapping("/song/{id}")
    public ApiResponse<Void> delete(@PathVariable long id){
        songService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Song")
                .build();
    }
    @GetMapping("/songs/sorted-by-viewer")
    public ApiResponse<PageResponse<SongResponse>> fetchAllSortedByViewer(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "desc") String viewerSortOrder) {

        return ApiResponse.<PageResponse<SongResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(songService.fetchAllSongSortedByViewer(pageNo, pageSize, viewerSortOrder))
                .message("Fetched All Songs Sorted by Viewer Count")
                .build();
    }
    @GetMapping("/songs/genre/{genreId}")
    public ApiResponse<List<SongResponse>> fetchSongsByGenre(@PathVariable long genreId) {

        return ApiResponse.<List<SongResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(songService.fetchSongsByGenre(genreId))
                .message("Fetched Songs by Genre")
                .build();
    }
}
