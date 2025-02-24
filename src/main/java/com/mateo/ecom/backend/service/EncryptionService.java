package com.mateo.ecom.backend.service;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    @Value("${encryption.salt.rounds}")
    private int saltrounds;
    private String salt;

    @PostConstruct
    public void init() {
        salt = BCrypt.gensalt(saltrounds);
    }

    public String encrypt(String password) {
        return BCrypt.hashpw(password, salt);
    }
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
