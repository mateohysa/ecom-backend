package com.mateo.ecom.backend.models.dao;

import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(AppUser user);

}
