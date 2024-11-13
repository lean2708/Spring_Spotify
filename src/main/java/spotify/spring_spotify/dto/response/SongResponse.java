package spotify.spring_spotify.dto.response;

import spotify.spring_spotify.dto.basic.AlbumBasic;
import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.basic.GenreBasic;
import spotify.spring_spotify.entity.*;
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
public class SongResponse {
    long id;
    String name;
    SongType type;
    String description;
    long duration;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    AlbumBasic album;

    Set<ArtistBasic> artists;

    GenreBasic genre;
}
