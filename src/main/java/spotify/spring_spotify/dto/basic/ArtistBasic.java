package spotify.spring_spotify.dto.basic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistBasic {
    long id;
    String name;
    long follower;
}
