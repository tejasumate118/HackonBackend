package com.fourloop.hackon_backend.service;

import com.fourloop.hackon_backend.dto.LoginRequest;
import com.fourloop.hackon_backend.dto.PasswordResetRequest;
import com.fourloop.hackon_backend.dto.SignUpRequest;
import com.fourloop.hackon_backend.model.PasswordResetToken;
import com.fourloop.hackon_backend.model.User;
import com.fourloop.hackon_backend.model.UserPrincipal;
import com.fourloop.hackon_backend.repository.PasswordResetTokenRepository;
import com.fourloop.hackon_backend.repository.UserRepository;
import com.fourloop.hackon_backend.utils.EmailService;
import com.fourloop.hackon_backend.utils.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Value("${frontend.url}")
    private String frontendUrl;
    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.authenticationManager = authenticationManager;

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        User user= userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new RuntimeException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal(user);

        return jwtService.generateToken(userPrincipal);
    }

    public boolean signup(SignUpRequest signUpRequest) {


        Optional<User> user = userRepository.findByEmail(signUpRequest.email());
        if (user.isPresent()) {
            return false;
        }
        // create user1
        User user1 = new User();
        user1.setName(signUpRequest.name());
        user1.setEmail(signUpRequest.email());
        user1.setPassword(passwordEncoder.encode(signUpRequest.password()));

        User saveUser = userRepository.save(user1);

        // Generate and send password reset token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, saveUser, LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(resetToken);

        // Send email with reset link
        String resetLink = frontendUrl+"/reset-password?token=" + token;
        emailService.setPasswordMail(saveUser.getEmail(), "Set Your Password", "Click here to set your password: " + resetLink);
        return true;
    }


    public ResponseEntity<Map<String,String>> resetPassword(String token, PasswordResetRequest request) {
        Optional<PasswordResetToken> optionalToken = Optional.ofNullable(passwordResetTokenRepository.findByToken(token));
        if(optionalToken.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));
        }
        PasswordResetToken resetToken = optionalToken.get();
        User user = resetToken.getUser();
        // Check if token is expired
        if(resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())){
            passwordResetTokenRepository.delete(resetToken);
            return ResponseEntity.status(400).body(Map.of("message","Token expired"));
        }
        user.setPassword(passwordEncoder.encode(request.password()));
        //if staff enable account

        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
        return ResponseEntity.status(200).body(Map.of("message","Password reset successfully"));
    }

    public String forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with this email does not exist.");
        }

        User user = userOptional.get();
        String token = generatePasswordResetToken(user);

        // Send email with password reset link
        String resetUrl = frontendUrl +"/reset-password?token=" + token;
        emailService.sendResetMail(user.getEmail(), "Reset Your Password",
                "Click here to reset your password: " + resetUrl);

        return "Password reset email sent.";
    }

    private String generatePasswordResetToken(User user) {
        // Check if the user exists in the users table
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User with ID " + user.getId() + " does not exist.");
        }

        // Delete any existing password reset token for the user
        passwordResetTokenRepository.findByUserId(user.getId()).ifPresent(passwordResetTokenRepository::delete);

        // Generate a new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(24));

        // Save the new token
        passwordResetTokenRepository.save(resetToken);

        return token;
    }
}
