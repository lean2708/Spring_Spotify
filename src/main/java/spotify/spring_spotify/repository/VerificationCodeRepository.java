package spotify.spring_spotify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spotify.spring_spotify.entity.VerificationCodeEntity;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {
    Optional<VerificationCodeEntity> findByEmailAndVerificationCode(String email, String verificationCode);
    void deleteByExpirationTimeBefore(long expirationTime);
}
