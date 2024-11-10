package com.mateo.ecom.backend.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mateo.ecom.backend.models.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String key;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryInSeconds}")
    private int expiryInSeconds;

    private Algorithm algorithm;

    public static final String USERNAME_KEY = "USERNAME";

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(key);
    }

    public String createToken(AppUser user) {
        return JWT.create()
                .withClaim(USERNAME_KEY, user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()  + (1000 + expiryInSeconds) ))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    //since we used .withclaim username_key in the method above
    //we can also use the same in this method below to find who is
    //sending requests to our api just by the JWT token

    public String findUsername(String token) {
        return JWT.decode(token).getClaim(USERNAME_KEY).asString();
    }
}
