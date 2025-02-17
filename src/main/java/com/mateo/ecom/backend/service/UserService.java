package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.api.exceptions.EmailFailureException;
import com.mateo.ecom.backend.api.exceptions.EmailNotFoundException;
import com.mateo.ecom.backend.api.exceptions.UserAlreadyExists;
import com.mateo.ecom.backend.api.exceptions.UserNotVerifiedException;
import com.mateo.ecom.backend.api.model.LoginBody;
import com.mateo.ecom.backend.api.model.PasswordResetBody;
import com.mateo.ecom.backend.api.model.RegistrationBody;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.VerificationToken;
import com.mateo.ecom.backend.models.dao.UserRepository;
import com.mateo.ecom.backend.models.dao.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private VerificationTokenRepository verificationTokenRepository;
    private EmailService emailService;




    public UserService(UserRepository userRepository, EncryptionService encryptionService, JWTService jwtService, VerificationTokenRepository  verificationTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    public AppUser user(RegistrationBody registrationBody) throws UserAlreadyExists, EmailFailureException {
        Optional<AppUser> existingEmail = userRepository.findByEmailIgnoreCase(registrationBody.getEmail());
        Optional<AppUser> existingUsername = userRepository.findByUsernameIgnoreCase(registrationBody.getUsername());
        
        System.out.println("Checking email: " + registrationBody.getEmail() + ", exists: " + existingEmail.isPresent());
        System.out.println("Checking username: " + registrationBody.getUsername() + ", exists: " + existingUsername.isPresent());
        
        if (existingEmail.isPresent() || existingUsername.isPresent()) {
            throw new UserAlreadyExists();
        }
        AppUser user = new AppUser();
        user.setEmail(registrationBody.getEmail());
        user.setUsername(registrationBody.getUsername());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setPassword(encryptionService.encrypt(registrationBody.getPassword()));
        VerificationToken verificationToken = generateVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        return userRepository.save(user);
    }

    /*public String logInUser(LoginBody loginBody){
        Optional<AppUser> optUser = userRepository.findByUsernameLikeIgnoreCase(loginBody.getUsername());
        if (optUser.isPresent()){
            AppUser user = optUser.get();
            if(encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())){
                return jwtService.createToken(user);
            }

        }
        return null;

    }*/
    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<AppUser> opUser = userRepository.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            AppUser user = opUser.get();
            if (encryptionService.checkPassword(loginBody.getPassword(), user.getPassword())) {
                if (user.getEmailVerified()) {
                    return jwtService.createToken(user);
                } else {
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();
                    boolean resend = verificationTokens.isEmpty() ||
                            verificationTokens.getFirst().getCreatedAt().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
                    if (resend) {
                        VerificationToken verificationToken = generateVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        return null;
    }
    private VerificationToken generateVerificationToken(AppUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.createToken(user));
        verificationToken.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            AppUser user = verificationToken.getUser();
            if (!user.getEmailVerified()) {
                user.setEmailVerified(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword (String email) throws EmailNotFoundException, EmailFailureException {
        Optional<AppUser> opUser = userRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            AppUser user = opUser.get();
            String token = jwtService.passwordResetVerificationToken(user);
            emailService.sendPasswordResetEmail(user,token);
        }else {
            throw new EmailNotFoundException();
        }
    }
    public void resetPassword(PasswordResetBody body){
        // get the email from the decoded JWT that we use just for resetting the password
        String email = jwtService.findPasswordResetEmail(body.getToken());
        Optional<AppUser> opUser = userRepository.findByEmailIgnoreCase(email);
        if (opUser.isPresent()) {
            AppUser user = opUser.get();
            user.setPassword(encryptionService.encrypt(body.getPassword()));
            userRepository.save(user);
        }
    }
}
