package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.GenreRequest;
import spotify.spring_spotify.dto.response.GenreResponse;
import spotify.spring_spotify.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/genres")
public class GenreController {
    private final GenreService genreService;

    @ApiMessage("Create Genre")
    @PostMapping()
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.create(request));
    }

    @ApiMessage("Fetch Genre By Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> fetchById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(genreService.fetchById(id));
    }
    @ApiMessage("Update Genre")
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> update(@PathVariable long id,@Valid @RequestBody GenreRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(genreService.update(id, request));
    }
    @ApiMessage("Delete Genre")
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable long id){
        genreService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
