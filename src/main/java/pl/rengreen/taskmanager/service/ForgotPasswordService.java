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

    public void initiateSuperAdmin(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String resetToken = tokenService.generateToken();
            user.setResetToken(resetToken);
            userRepository.save(user);
            sendResetEmailForSuperAdmin(user.getEmail(), resetToken);
        }
    }

    // Send reset password email with token
    private void sendResetEmail(String email, String token) {
        // Construct and send the reset password email with a link containing the token
        String resetLink = "http://localhost:1111/reset-password?token=" + token;

        String emailSubject = "Password Reset - Pro Collab";
        String emailContent = "<html><body>" +
                "<h2>Pro Collab Password Reset</h2>" +
                "<p>Hello,</p>" +
                "<p>You recently requested to reset your password for your Pro Collab account.</p>" +
                "<p>To reset your password, please click the link below:</p>" +
                "<p><a href=\"" + resetLink + "\">Reset Password</a></p>" +
                "<p>If you did not request a password reset, please ignore this email.</p>" +
                "<p>Thank you,</p>" +
                "<p>Pro Collab Team</p>" +
                "</body></html>";

        emailService.sendEmail(email, emailSubject, emailContent);
    }
    private void sendResetEmailForSuperAdmin(String email, String token) {
        // Construct and send the reset password email with a link containing the token
        String resetLink = "http://localhost:1111/reset-password?token=" + token;

        String emailSubject = "Super Admin Created - Pro Collab";
        String emailContent = "<html><body>" +
                "<h2>Pro Collab Password Reset</h2>" +
                "<p>Hello,</p>" +
                "<p>This is your Superadmin Credentials Pro Collab account.</p>" +
                "<p>To continue first enter your password, please click the link below:</p>" +
                "<b>Username :"+email+
                "<p><a href=\"" + resetLink + "\"> Create Password</a></p>" +
                "<p>Enter your password here</p>" +
                "<p>Thank you,</p>" +
                "<p>Pro Collab Team</p>" +
                "</body></html>";

        emailService.sendEmail(email, emailSubject, emailContent);
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
