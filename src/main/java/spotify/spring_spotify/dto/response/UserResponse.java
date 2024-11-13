package spotify.spring_spotify.dto.response;

import spotify.spring_spotify.dto.basic.PlaylistBasic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
     long id;
     String name;
     String email;
     @JsonFormat(pattern = "dd/MM/yyyy")
     LocalDate dob;
     String gender;
     String language;
     Set<PlaylistBasic> playlists;
     @JsonFormat(pattern = "dd/MM/yyyy")
     private LocalDate createdAt;
     @JsonFormat(pattern = "dd/MM/yyyy")
     private LocalDate updatedAt;
}
