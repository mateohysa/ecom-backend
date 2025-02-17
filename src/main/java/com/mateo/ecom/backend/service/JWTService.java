package com.mateo.ecom.backend.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    public static final String VERIFY_EMAIL_KEY = "VERIFY_EMAIL";
    public static final String RESET_PASSWORD_EMAIL_KEY = "RESET_PASSWORD_EMAIL";


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

    //this will generate a JWT where it only has a claim over the email
    public String createVerificationToken(AppUser user) {
        return JWT.create()
                .withClaim(VERIFY_EMAIL_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()  + (1000 + expiryInSeconds) ))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String passwordResetVerificationToken(AppUser user) {
        return JWT.create()
                .withClaim(RESET_PASSWORD_EMAIL_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()  + (1000 + expiryInSeconds) ))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String findPasswordResetEmail(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim(RESET_PASSWORD_EMAIL_KEY).asString();
    }

    //since we used .withclaim username_key in the method above
    //we can also use the same in this method below to find who is
    //sending requests to our api just by the JWT token

    public String findUsername(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim(USERNAME_KEY).asString();
    }
}
