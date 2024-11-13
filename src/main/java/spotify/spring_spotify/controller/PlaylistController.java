package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.PlaylistRequest;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    @PostMapping()
    @ApiMessage("Create playlist")
    public ResponseEntity<PlaylistResponse> create(@Valid @RequestBody PlaylistRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.create(request));
    }

    @ApiMessage("Fetch Playlist By Id")
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> fetchById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(playlistService.fetchById(id));
    }
    @ApiMessage("Update Playlist")
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistResponse> update(@PathVariable long id,@Valid @RequestBody PlaylistRequest request){

        return ResponseEntity.status(HttpStatus.OK).body(playlistService.update(id, request));
    }
    @ApiMessage("Delete Playlist")
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable long id){
        playlistService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
