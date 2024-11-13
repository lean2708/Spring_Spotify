package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.SongRequest;
import spotify.spring_spotify.dto.response.SongResponse;
import spotify.spring_spotify.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/songs")
public class SongController {
    private final SongService songService;
    @ApiMessage("Create Song")
    @PostMapping
    private ResponseEntity<SongResponse> create(@Valid  @RequestBody SongRequest songRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.create(songRequest));
    }
    @ApiMessage("Fetch Song By Id")
    @GetMapping("{id}")
    public ResponseEntity<SongResponse> fetchById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(songService.fetchById(id));
    }
    @ApiMessage("Update Song")
    @PutMapping("{id}")
    public ResponseEntity<SongResponse> update(@PathVariable long id,@Valid @RequestBody SongRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(songService.update(id,request));
    }
    @ApiMessage("Delete Song")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        songService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
