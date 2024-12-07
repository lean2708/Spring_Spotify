package spotify.spring_spotify.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import spotify.spring_spotify.dto.request.AlbumRequest;
import spotify.spring_spotify.dto.response.AlbumResponse;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AlbumController {
private final AlbumService albumService;


    @PostMapping(value = "/album",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AlbumResponse> create(@Valid @ModelAttribute AlbumRequest request,
                                             @RequestParam(value = "image", required = false) MultipartFile multipartFile)
            throws FileException, IOException, SAXException {
        System.out.println("Album Name: " + request.getName());
        return ApiResponse.<AlbumResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(albumService.create(request, multipartFile))
                .message("Create Album")
                .build();
    }

    @GetMapping("/album/{id}")
    public ApiResponse<AlbumResponse> fetchById(@PathVariable long id){
        return ApiResponse.<AlbumResponse>builder()
                .code(HttpStatus.OK.value())
                .result(albumService.fetchById(id))
                .message("Fetch Album By Id")
                .build();
    }
    @GetMapping("/albums")
    public ApiResponse<List<AlbumResponse>> fetchAll(){
        return ApiResponse.<List<AlbumResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(albumService.fetchAllAlbum())
                .message("Fetch All Album")
                .build();
    }
    @GetMapping("/albums/search")
    public ApiResponse<PageResponse<AlbumResponse>> searchAlbums(@RequestParam @NotBlank String name,
                                                                   @RequestParam(defaultValue = "1") int pageNo,
                                                                   @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<AlbumResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(albumService.searchAlbums(name, pageNo, pageSize))
                .message("Search Album By Name With Pagination")
                .build();
    }
    @PutMapping(value = "/album/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AlbumResponse> update(@PathVariable long id,@Valid @ModelAttribute AlbumRequest request,
                                             @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException, SAXException {
        return ApiResponse.<AlbumResponse>builder()
                .code(HttpStatus.OK.value())
                .result(albumService.update(id, request, multipartFile))
                .message("Update Album")
                .build();
    }
    @DeleteMapping("/album/{id}")
    private ApiResponse<Void> delete(@PathVariable long id){
        albumService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Album")
                .build();
    }
}
