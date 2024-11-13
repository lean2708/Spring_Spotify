package spotify.spring_spotify.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "Name không được để trống")
    String name;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email phải có định dạng hợp lệ")
    String email;
    @Size(min = 8, message = "Password phải từ 8 kí tự trở lên")
    String password;
    @JsonFormat(pattern = "dd/MM/yyyy")
    LocalDate dob;
    String language;
    String gender;
    Set<String> playlists;
}
