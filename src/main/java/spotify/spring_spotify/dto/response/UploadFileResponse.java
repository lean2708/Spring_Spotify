package spotify.spring_spotify.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileResponse {
    private String fileName;
    private Instant uploadedAt;
}
