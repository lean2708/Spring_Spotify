package spotify.spring_spotify.dto.response;

import spotify.spring_spotify.dto.basic.AlbumBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistResponse {
    long id;
    String name;
    long follower;
    String imageURL;

    Set<SongBasic> songs;

    Set<AlbumBasic> albums;
}
