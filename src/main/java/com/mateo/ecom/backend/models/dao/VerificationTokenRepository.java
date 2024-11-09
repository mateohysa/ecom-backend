package com.mateo.ecom.backend.models.dao;

import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {


    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(AppUser user);
}
