package com.mateo.ecom.backend.models.dao;


import com.mateo.ecom.backend.models.AppUser;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsernameLikeIgnoreCase(String username);
    Optional<AppUser> findByEmailIgnoreCase(String email);
}
