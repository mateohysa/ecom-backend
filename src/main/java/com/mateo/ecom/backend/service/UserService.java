package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.api.exceptions.UserAlreadyExists;
import com.mateo.ecom.backend.api.model.LoginBody;
import com.mateo.ecom.backend.api.model.RegistrationBody;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.dao.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    EncryptionService encryptionService;
    private JWTService jwtService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService, JWTService jwtService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public AppUser user(RegistrationBody registrationBody) throws UserAlreadyExists {

        if (userRepository.findByUsernameLikeIgnoreCase(registrationBody.getUsername()).isPresent() || userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExists();
        }
        AppUser user = new AppUser();
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.encrypt(registrationBody.getPassword()));
        user.setUsername(registrationBody.getUsername());

        return userRepository.save(user);
    }

    public String logInUser(LoginBody loginBody){
        Optional<AppUser> optUser = userRepository.findByUsernameLikeIgnoreCase(loginBody.getUsername());
        if (optUser.isPresent()){
            AppUser user = optUser.get();
            if(encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())){
                return jwtService.createToken(user);
            }

        }
        return null;

    }
}
