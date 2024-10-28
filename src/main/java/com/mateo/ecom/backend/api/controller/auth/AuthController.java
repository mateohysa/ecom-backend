package com.mateo.ecom.backend.api.controller.auth;

import com.mateo.ecom.backend.api.model.RegistrationBody;
import com.mateo.ecom.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody RegistrationBody body) {
        userService.user(body);
    }

}
