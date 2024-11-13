package spotify.spring_spotify.dto.basic;

import spotify.spring_spotify.entity.SongType;
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
    SongType type;
    String description;
    long duration;

}
