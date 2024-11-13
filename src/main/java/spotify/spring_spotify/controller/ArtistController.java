package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.ArtistRequest;
import spotify.spring_spotify.dto.response.ArtistResponse;
import spotify.spring_spotify.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/artists")
public class ArtistController {
private final ArtistService artistService;
    @ApiMessage("Create Artist")
    @PostMapping()
    public ResponseEntity<ArtistResponse> create(@Valid @RequestBody ArtistRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.create(request));
    }

    @ApiMessage("Fetch Artist By Id")
    @GetMapping("{id}")
    public ResponseEntity<ArtistResponse> fetchById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(artistService.fetchById(id));
    }
    @ApiMessage("Update Artist")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponse> update(@PathVariable long id,@Valid @RequestBody ArtistRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(artistService.update(id, request));
    }
    @ApiMessage("Delete Artist")
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable long id){
        artistService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
