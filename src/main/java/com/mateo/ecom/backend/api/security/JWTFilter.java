package com.mateo.ecom.backend.api.security;

import org.springframework.stereotype.Component;

@Component
public class JWTFilter {

    //when the request happens we need to take the token out of the request
    //
    //validate the token
    //
    //if the token is valid we can get the user object from the db
    // that  relates to the username in the token
    //
    //assign the user to the security session while the request is processed
    //
    //
}
