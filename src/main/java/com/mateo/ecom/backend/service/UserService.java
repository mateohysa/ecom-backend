package com.mateo.ecom.backend.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.mateo.ecom.backend.api.exceptions.EmailFailureException;
import com.mateo.ecom.backend.api.exceptions.UserAlreadyExists;
import com.mateo.ecom.backend.api.exceptions.UserNotVerifiedException;
import com.mateo.ecom.backend.api.model.LoginBody;
import com.mateo.ecom.backend.api.model.RegistrationBody;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.VerificationToken;
import com.mateo.ecom.backend.models.dao.UserRepository;
import com.mateo.ecom.backend.models.dao.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    EncryptionService encryptionService;
    private JWTService jwtService;
    private VerificationTokenRepository verificationTokenRepository;
    private EmailService emailService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService, JWTService jwtService, VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    public AppUser user(RegistrationBody registrationBody) throws UserAlreadyExists, EmailFailureException {

        if (userRepository.findByUsernameLikeIgnoreCase(registrationBody.getUsername()).isPresent() || userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExists();
        }
        AppUser user = new AppUser();
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.encrypt(registrationBody.getPassword()));
        user.setUsername(registrationBody.getUsername());

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendEmail(verificationToken);


        return userRepository.save(user);
    }

    public String logInUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<AppUser> optUser = userRepository.findByUsernameLikeIgnoreCase(loginBody.getUsername());
        if (optUser.isPresent()){
            AppUser user = optUser.get();
            if(encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())){
                if(user.getEmailVerified()){
                return jwtService.createToken(user);}
                else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 || verificationTokens.get(0).getCreatedAt().before(new Timestamp(System.currentTimeMillis()-(60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException (resend);
                }
            }

        }
        return null;

    }
    private VerificationToken createVerificationToken (AppUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationToken(user));
        verificationToken.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);

        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }
    @Transactional
    public boolean verifyUser(String token){
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            AppUser user = verificationToken.getUser();
            if(!user.getEmailVerified()){
                user.setEmailVerified(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

}
