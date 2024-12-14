package spotify.spring_spotify.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import spotify.spring_spotify.dto.basic.PlaylistBasic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
     long id;
     String name;
     String email;
     String imageURL;
     @JsonFormat(pattern = "dd/MM/yyyy")
     LocalDate dob;
     Set<PlaylistBasic> createdPlaylists;
     @JsonInclude(JsonInclude.Include.NON_NULL)
     List<Long> savedPlaylistId;

     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonFormat(pattern = "dd/MM/yyyy")
     private LocalDate createdAt;
     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonFormat(pattern = "dd/MM/yyyy")
     private LocalDate updatedAt;

     Set<RoleResponse> roles;

     boolean premiumStatus;

     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonFormat(pattern = "dd/MM/yyyy")
     private LocalDate premiumExpiryDate;
}
