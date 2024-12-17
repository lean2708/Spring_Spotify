package spotify.spring_spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import spotify.spring_spotify.dto.basic.SongBasic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
    int totalTracks;
    long follower;
    String creator;
    String imageURL;
    long listener;
    double totalHours;

    Set<SongResponse> songs;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedAt;

}
