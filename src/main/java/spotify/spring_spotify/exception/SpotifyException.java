package spotify.spring_spotify.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpotifyException extends RuntimeException{
    private ErrorCode errorCode;
    public SpotifyException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
