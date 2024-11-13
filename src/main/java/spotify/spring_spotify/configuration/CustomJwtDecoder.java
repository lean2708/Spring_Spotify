package spotify.spring_spotify.configuration;

import spotify.spring_spotify.dto.request.TokenRequest;
import spotify.spring_spotify.service.AuthService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;
    @Autowired
    private AuthService authService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authService.introspect(TokenRequest.builder().token(token).build());
            if (!response.isValid()) {
                throw new JwtException("Token không hợp lệ");
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException("Xác thực token thất bại: " + e.getMessage());
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SINGER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        try {
            return nimbusJwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new JwtException("Lỗi giải mã token: " + e.getMessage());
        }
    }
}
