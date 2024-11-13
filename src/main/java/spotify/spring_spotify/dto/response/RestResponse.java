package spotify.spring_spotify.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestResponse<T> {
     int statusCode;
     String error;
     Object message;
     T data;
}
