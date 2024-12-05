package spotify.spring_spotify.dto.request;

import spotify.spring_spotify.entity.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongRequest {
    @NotBlank(message = "Name không được để trống")
     String name;
     String description;
    double duration;
    long listener;

    String album;

    Set<String> artists;

    String genre;

}
