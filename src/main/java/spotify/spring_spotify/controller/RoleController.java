package spotify.spring_spotify.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spotify.spring_spotify.dto.request.RoleRequest;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.service.RoleService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class RoleController {
    private final RoleService roleService;
    @PostMapping("/role")
    public ApiResponse<RoleResponse> create(@Valid @RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(roleService.create(request))
                .message("Create Role")
                .build();
    }
    @GetMapping("/roles")
    public ApiResponse<List<RoleResponse>> fetchAll(){
        return ApiResponse.<List<RoleResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(roleService.fetchAll())
                .message("Fetch All Role")
                .build();
    }
    @DeleteMapping("/role/{name}")
    public ApiResponse<Void> delete(@PathVariable String name){
        roleService.delete(name);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Role")
                .build();
    }
}
