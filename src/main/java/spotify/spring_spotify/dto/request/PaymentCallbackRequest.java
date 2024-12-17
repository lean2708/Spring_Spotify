package spotify.spring_spotify.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCallbackRequest {
    String responseCode;
    long amount;
}
