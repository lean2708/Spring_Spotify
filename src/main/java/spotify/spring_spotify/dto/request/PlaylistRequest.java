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
public class PlaylistRequest {
    @NotBlank(message = "Title không được để trống")
    String title;
    String description;
    long follower;
    long listener;
    String creator;

    Set<String> songs;
}
