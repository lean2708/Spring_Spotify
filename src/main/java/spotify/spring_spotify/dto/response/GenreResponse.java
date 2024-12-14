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
public class GenreResponse {
    long id;
    String key;
    String name;

}
