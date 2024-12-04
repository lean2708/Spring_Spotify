package spotify.spring_spotify.dto.response;

import spotify.spring_spotify.dto.basic.ArtistBasic;
import spotify.spring_spotify.dto.basic.SongBasic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import spotify.spring_spotify.entity.Genre;

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
    long follower;
    String imageURL;
    double totalHours;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

    Set<ArtistBasic> artists;

    Set<SongBasic> songs;
}
