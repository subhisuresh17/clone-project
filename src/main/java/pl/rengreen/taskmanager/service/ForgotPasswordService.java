package pl.rengreen.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.UserRepository;

import java.util.Optional;

@Service
public class ForgotPasswordService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    public ForgotPasswordService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
            TokenService tokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    // Initiate password reset process
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String resetToken = tokenService.generateToken();
            user.setResetToken(resetToken);
            userRepository.save(user);
            sendResetEmail(user.getEmail(), resetToken);
        }
    }

    // Send reset password email with token
    private void sendResetEmail(String email, String token) {
        // Construct and send the reset password email with a link containing the token
        String resetLink = "http://localhost:1111/reset-password?token=" + token;
        String emailContent = "Click the following link to reset your password: " + resetLink;
        emailService.sendEmail(email, "Password Reset", emailContent);
    }

    // Verify the reset token
    public User verifyResetToken(String token) {
        return userRepository.findByResetToken(token);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user != null) {
            // Encode the new password
            String encodedPassword = passwordEncoder.encode(newPassword);

            // Set the encoded password
            user.setPassword(encodedPassword);

            // Clear the reset token
            user.setResetToken(null);

            // Save the user
            userRepository.save(user);
        }
    }
}
