package spotify.spring_spotify.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequest {
    @NotBlank(message = "Name không được để trống")
    String name;
    String description;
    int totalTracks;

    Set<String> artists;

    Set<String> genres;

    Set<String> songs;

}
