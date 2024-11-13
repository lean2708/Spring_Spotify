package spotify.spring_spotify.controller;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.request.AuthRequest;
import spotify.spring_spotify.dto.request.TokenRequest;
import spotify.spring_spotify.dto.request.UserRequest;
import spotify.spring_spotify.dto.response.AuthResponse;
import spotify.spring_spotify.dto.response.IntrospectResponse;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.service.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    @ApiMessage("TOKEN")
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) throws JOSEException {
        AuthResponse result = authService.authenticated(request);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @PostMapping("/register")
    @ApiMessage("Register a new User")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @ApiMessage("Introspect")
    @PostMapping("/introspect")
    private ResponseEntity<IntrospectResponse> introspected(@RequestBody TokenRequest request) throws ParseException, JOSEException {
        IntrospectResponse introspectResponse = authService.introspect(request);
        return ResponseEntity.status(HttpStatus.OK).body(introspectResponse);
    }
    @ApiMessage("LOGOUT")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody TokenRequest request) throws JOSEException, ParseException {
         authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
