package spotify.spring_spotify.dto.response;

import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.basic.GenreBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponse {
    long id;
    String name;
    String description;
    int totalTracks;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    Set<ArtistBasic> artists;

    Set<GenreBasic> genres;

    Set<SongBasic> songs;
}
