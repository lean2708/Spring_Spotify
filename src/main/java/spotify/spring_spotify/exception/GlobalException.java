package spotify.spring_spotify.exception;

import org.springframework.security.oauth2.jwt.JwtException;
import spotify.spring_spotify.dto.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class GlobalException {
    // handle all exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleAllException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
    @ExceptionHandler({SpotifyException.class})
    public ResponseEntity<RestResponse<Object>> handlingSpotifyException(SpotifyException spotifyException){
        RestResponse<Object> response = new RestResponse<>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setError("Exception Occurs...");
        response.setMessage(spotifyException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    //  Xử lý các lỗi xác thực
    @ExceptionHandler(value = {MethodArgumentNotValidException.class,
            JwtException.class})
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex){
//       lấy thông tin về tất cả các lỗi xác thực xảy ra trong quá trình kiểm tra
        BindingResult result = ex.getBindingResult();
//        lấy danh sách các lỗi cụ thể cho từng trường
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getDefaultMessage());
        }
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
