package spotify.spring_spotify.dto.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongBasic {
    long id;
    String name;
    String description;
    long duration;
    long listener;
    String imageURL;
    String fileSongURL;
}
