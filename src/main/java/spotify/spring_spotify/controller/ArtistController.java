package spotify.spring_spotify.controller;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.request.ArtistRequest;
import spotify.spring_spotify.dto.response.*;
import spotify.spring_spotify.exception.FileException;
import spotify.spring_spotify.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/artists")
public class ArtistController {
private final ArtistService artistService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ArtistResponse> create(@Valid @ModelAttribute ArtistRequest request,
                                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException {
        return ApiResponse.<ArtistResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(artistService.create(request, multipartFile))
                .message("Create Artist")
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<ArtistResponse> fetchById(@PathVariable long id){
        return ApiResponse.<ArtistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.fetchById(id))
                .message("Fetch Artist By Id")
                .build();
    }

    @GetMapping()
    public ApiResponse<List<ArtistResponse>> fetchAll(){
        return ApiResponse.<List<ArtistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.fetchAllAritst())
                .message("Fetch All Artist")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<ArtistResponse>> searchArtists(@RequestParam String name,
                                                                @RequestParam(defaultValue = "1") int pageNo,
                                                                @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<ArtistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.searchArtists(name, pageNo, pageSize))
                .message("Search Artist By Name With Pagination")
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ArtistResponse> update(@PathVariable long id,@Valid @ModelAttribute ArtistRequest request,
                                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException {
        return ApiResponse.<ArtistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.update(id, request,multipartFile))
                .message("Update Artist")
                .build();
    }
    @DeleteMapping("/{id}")
    private ApiResponse<Void> delete(@PathVariable long id){
        artistService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Artist")
                .build();
    }
}
