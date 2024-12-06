package spotify.spring_spotify.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotify.spring_spotify.dto.request.PermissionRequest;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.PermissionResponse;
import spotify.spring_spotify.dto.response.PlaylistResponse;
import spotify.spring_spotify.service.PermissionService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/permission")
    public ApiResponse<PermissionResponse> create(@Valid @RequestBody PermissionRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(permissionService.create(request))
                .message("Create Permission")
                .build();
    }
    @GetMapping("/permissions")
    public ApiResponse<List<PermissionResponse>> fetchAll(){
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(permissionService.fetchAll())
                .message("Fetch All Permission")
                .build();
    }
    @DeleteMapping("/permission/{name}")
    public ApiResponse<Void> delete(@PathVariable String name){
        permissionService.delete(name);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Permission")
                .build();
    }
}
