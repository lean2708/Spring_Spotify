package spotify.spring_spotify.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
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
@RequestMapping("/v1")
public class ArtistController {
private final ArtistService artistService;
    @PostMapping(value = "/artist",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ArtistResponse> create(@Valid @ModelAttribute ArtistRequest request,
                                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException, SAXException {
        return ApiResponse.<ArtistResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(artistService.create(request, multipartFile))
                .message("Create Artist")
                .build();
    }

    @GetMapping("/artist/{id}")
    public ApiResponse<ArtistResponse> fetchById(@PathVariable long id){
        return ApiResponse.<ArtistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.fetchById(id))
                .message("Fetch Artist By Id")
                .build();
    }

    @GetMapping("/artists")
    public ApiResponse<PageResponse<ArtistResponse>> fetchAll(@RequestParam(defaultValue = "1") int pageNo,
                                                      @RequestParam(defaultValue = "3") int pageSize,
                                                      @RequestParam(defaultValue = "asc") String nameSortOrder){
        return ApiResponse.<PageResponse<ArtistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.fetchAllAritsts(pageNo, pageSize, nameSortOrder))
                .message("Fetch All Artist With Pagination")
                .build();
    }

    @GetMapping("artists/search")
    public ApiResponse<PageResponse<ArtistResponse>> searchArtists(@RequestParam @NotBlank String name,
                                                                @RequestParam(defaultValue = "1") int pageNo,
                                                                @RequestParam(defaultValue = "3") int pageSize){
        return ApiResponse.<PageResponse<ArtistResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.searchArtists(name, pageNo, pageSize))
                .message("Search Artist By Name With Pagination")
                .build();
    }

    @PutMapping(value = "/artist/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ArtistResponse> update(@PathVariable long id,@Valid @ModelAttribute ArtistRequest request,
                                              @RequestParam(value = "image", required = false) MultipartFile multipartFile) throws FileException, IOException, SAXException {
        return ApiResponse.<ArtistResponse>builder()
                .code(HttpStatus.OK.value())
                .result(artistService.update(id, request,multipartFile))
                .message("Update Artist")
                .build();
    }
    @DeleteMapping("/artist/{id}")
    private ApiResponse<Void> delete(@PathVariable long id){
        artistService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Artist")
                .build();
    }
}
