package com.mateo.ecom.backend.api.controller.auth;

import com.mateo.ecom.backend.api.exceptions.EmailFailureException;
import com.mateo.ecom.backend.api.exceptions.UserAlreadyExists;
import com.mateo.ecom.backend.api.exceptions.UserNotVerifiedException;
import com.mateo.ecom.backend.api.model.LoginBody;
import com.mateo.ecom.backend.api.model.LoginResponse;
import com.mateo.ecom.backend.api.model.RegistrationBody;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    UserService userService;

    public AuthController( UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationBody body) {
        try {
            userService.user(body);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExists | EmailFailureException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody body) throws UserNotVerifiedException, EmailFailureException {
        String jwt = userService.loginUser(body);
            if (jwt == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }else {
                //created this new object so that in the future,
                //if logging in requires more details I won't need to do
                //a lot of code change to implement the new details.
                LoginResponse response = new LoginResponse();
                response.setToken(jwt);
                return ResponseEntity.ok(response);
            }


    }

    @GetMapping("/me")
    public AppUser getCurrentUser(@AuthenticationPrincipal AppUser user) {
        return user;
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if (userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
