package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.AlbumRequest;
import spotify.spring_spotify.dto.response.AlbumResponse;
import spotify.spring_spotify.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/albums")
public class AlbumController {
private final AlbumService albumService;
    @ApiMessage("Create Ablum")
    @PostMapping()
    public ResponseEntity<AlbumResponse> create(@Valid @RequestBody AlbumRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(request));
    }

    @ApiMessage("Fetch Ablum By Id")
    @GetMapping("{id}")
    public ResponseEntity<AlbumResponse> fetchById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(albumService.fetchById(id));
    }
    @ApiMessage("Update Album")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(@PathVariable long id,@Valid @RequestBody AlbumRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(albumService.update(id, request));
    }
    @ApiMessage("Delete Album")
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable long id){
        albumService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
