package spotify.spring_spotify.dto.response;

import spotify.spring_spotify.dto.basic.SongBasic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistResponse {
    long id;
    String title;
    String description;

    Set<SongBasic> songs;

}
