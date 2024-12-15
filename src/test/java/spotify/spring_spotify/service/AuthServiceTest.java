package spotify.spring_spotify.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import spotify.spring_spotify.dto.request.RegisterRequest;
import spotify.spring_spotify.dto.response.RoleResponse;
import spotify.spring_spotify.dto.response.UserResponse;
import spotify.spring_spotify.entity.Role;
import spotify.spring_spotify.entity.User;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.repository.RoleRepository;
import spotify.spring_spotify.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@TestPropertySource("/test.properties")
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private EmailService emailService;
    private RegisterRequest request;
    private User user;
    private UserResponse userResponse;
    private LocalDate dob;


    @BeforeEach
    void initData() {
        Role userRole = new Role("USER", "User Role", null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dob = LocalDate.parse("27/08/2004", formatter);

        request = RegisterRequest.builder()
                .name("Lê Văn An")
                .email("lean270804@gmail.com")
                .password("123456789")
                .dob(dob)
                .build();

        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        userResponse = UserResponse.builder()
                .id(4)
                .name("Lê Văn An")
                .email("lean270804@gmail.com")
                .dob(dob)
                .roles(Set.of(new RoleResponse("USER", "User Role", null)))
                .premiumStatus(false)
                .build();

        user = User.builder()
                .id(4)
                .name("Lê Văn An")
                .email("lean270804@gmail.com")
                .dob(dob)
                .roles(Set.of(userRole))
                .premiumStatus(false)
                .build();
    }

    @Test
    void register_validRequest_success() throws SpotifyException {
        // GIVEN
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.doNothing().when(emailService).sendUserEmailWithRegister(Mockito.any());
        // WHEN
        var response = authService.register(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo(4);
        Assertions.assertThat(response.getEmail()).isEqualTo("lean270804@gmail.com");
        Mockito.verify(emailService, Mockito.times(1)).sendUserEmailWithRegister(Mockito.any());
    }

    @Test
    void createUser_userExisted_fail() throws SpotifyException {
        // GIVEN
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        // WHEN
        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                SpotifyException.class, () -> authService.register(request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1007);
    }

}
