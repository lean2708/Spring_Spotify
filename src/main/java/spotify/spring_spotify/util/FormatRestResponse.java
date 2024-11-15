package spotify.spring_spotify.util;

import spotify.spring_spotify.annotation.ApiMessage;
import spotify.spring_spotify.dto.response.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();

        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(status);

        if(body instanceof String || body instanceof Resource){ // phan hoi la file hoac string
            return body;
        }
        if(status >= 400){
            return body;
        }
        else {
            restResponse.setData(body);
            //Lấy thông tin từ annotation @ApiMessage
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            // Nếu annotation tồn tại trên method, lấy message từ annotation
            restResponse.setMessage(message != null ? message.value() : "CALL API SUCCESS");
        }
        return restResponse;
    }
}
