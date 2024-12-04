package spotify.spring_spotify.controller;

import org.springframework.web.bind.annotation.*;
import spotify.spring_spotify.dto.request.AuthRequest;
import spotify.spring_spotify.dto.request.RegisterRequest;
import spotify.spring_spotify.dto.request.TokenRequest;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.ApiResponse;
import spotify.spring_spotify.dto.response.AuthResponse;
import spotify.spring_spotify.dto.response.IntrospectResponse;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ApiResponse<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) throws JOSEException {
        return ApiResponse.<AuthResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(authService.authenticated(request))
                .message("Token")
                .build();
    }
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(authService.register(request))
                .message("Register")
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authService.getMyInfo())
                .message("My Info")
                .build();
    }

    @PostMapping("/introspect")
    private ApiResponse<IntrospectResponse> introspected(@Valid @RequestBody TokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .code(HttpStatus.OK.value())
                .result(authService.introspect(request))
                .message("Introspect")
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody TokenRequest request) throws JOSEException, ParseException {
         authService.logout(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Logout")
                .build();
    }

}
