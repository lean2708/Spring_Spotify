package spotify.spring_spotify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spotify.spring_spotify.dto.request.ChangePasswordRequest;
import spotify.spring_spotify.dto.request.ForgotPasswordRequest;
import spotify.spring_spotify.entity.User;
import spotify.spring_spotify.entity.VerificationCodeEntity;
import spotify.spring_spotify.exception.ErrorCode;
import spotify.spring_spotify.exception.SpotifyException;
import spotify.spring_spotify.repository.UserRepository;
import spotify.spring_spotify.repository.VerificationCodeRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ForgotPasswordService {
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public VerificationCodeEntity forgotPassword(ForgotPasswordRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        String verificationCode = generateVerificationCode();
        try {
            emailService.sendVerificationCode(user, verificationCode);

            long expirationTime = System.currentTimeMillis() / 1000 + (5 * 60);

            VerificationCodeEntity verificationCodeEntity = VerificationCodeEntity.builder()
                    .email(user.getEmail().trim())
                    .verificationCode(verificationCode.trim())
                    .expirationTime(expirationTime)
                    .build();

            verificationCodeRepository.save(verificationCodeEntity);

            scheduler.schedule(() -> deleteVerificationCode(verificationCodeEntity.getId()), 15, TimeUnit.MINUTES);

            return verificationCodeEntity;
        } catch (Exception e) {
            log.error("Lỗi gửi email: ", e);
            throw new SpotifyException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public boolean verifyCode(String email, String verificationCode) {
            VerificationCodeEntity verificationCodeEntity = verificationCodeRepository.findByEmailAndVerificationCode(email, verificationCode)
                    .orElseThrow(() -> new SpotifyException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

            if (verificationCodeEntity.getExpirationTime() < System.currentTimeMillis() / 1000) {
                throw new SpotifyException(ErrorCode.VERIFICATION_CODE_EXPIRED);
            }

            return true;
    }

    private String generateVerificationCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    public void deleteVerificationCode(Long verificationCodeId) {
        VerificationCodeEntity verificationCodeEntity = verificationCodeRepository.findById(verificationCodeId)
                .orElseThrow(() -> new SpotifyException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

        verificationCodeRepository.delete(verificationCodeEntity);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new SpotifyException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
