package spotify.spring_spotify.controller;

import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.dto.response.PageResponse;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class GenreController {
    private final GenreService genreService;

    @PostMapping("/genre")
    public ApiResponse<GenreResponse> create(@Valid @RequestBody GenreRequest request){
        return ApiResponse.<GenreResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(genreService.create(request))
                .message("Create Genre")
                .build();
    }

    @GetMapping("/genre/{id}")
    public ApiResponse<GenreResponse> fetchById(@PathVariable long id){
        return ApiResponse.<GenreResponse>builder()
                .code(HttpStatus.OK.value())
                .result(genreService.fetchById(id))
                .message("Fetch Genre By Id")
                .build();
    }
    @GetMapping("/genres")
    public ApiResponse<PageResponse<GenreResponse>> fetchAll(@RequestParam(defaultValue = "1") int pageNo,
                                                             @RequestParam(defaultValue = "3") int pageSize,
                                                             @RequestParam(defaultValue = "asc") String nameSortOrder){
        return ApiResponse.<PageResponse<GenreResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(genreService.fetchAllGenre(pageNo, pageSize, nameSortOrder))
                .message("Fetch All Genre With Pagination")
                .build();
    }
    @PutMapping("/genre/{id}")
    public ApiResponse<GenreResponse> update(@PathVariable long id,@Valid @RequestBody GenreRequest request){
        return ApiResponse.<GenreResponse>builder()
                .code(HttpStatus.OK.value())
                .result(genreService.update(id, request))
                .message("Update Genre")
                .build();
    }
    @DeleteMapping("/genre/{id}")
    private ApiResponse<Void> delete(@PathVariable long id){
        genreService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Genre")
                .build();
    }
}
