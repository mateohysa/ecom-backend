package com.mateo.ecom.backend.models.dao;


import com.mateo.ecom.backend.models.AppUser;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

}
