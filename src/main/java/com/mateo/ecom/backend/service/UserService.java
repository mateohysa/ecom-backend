package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.api.exceptions.UserAlreadyExists;
import com.mateo.ecom.backend.api.model.RegistrationBody;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.dao.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser user(RegistrationBody registrationBody) throws UserAlreadyExists {

        if (userRepository.findByUsernameLikeIgnoreCase(registrationBody.getUsername()).isPresent() || userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExists();
        }
        AppUser user = new AppUser();
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(registrationBody.getPassword());
        user.setUsername(registrationBody.getUsername());

        return userRepository.save(user);
    }
}
