package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @ApiMessage("Create User")
    @PostMapping("")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }
    @ApiMessage("Fetch User By Id")
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> fetchById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchById(id));
    }
    @ApiMessage("Fetch All User")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> fetchAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchAll());
    }
    @ApiMessage("Update User")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable long id,@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, request));
    }
    @ApiMessage("Delete User")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
