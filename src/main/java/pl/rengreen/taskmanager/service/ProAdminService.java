package pl.rengreen.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.UserRepository;

@Service
public class ProAdminService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    public ProAdminService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
            TokenService tokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.passwordEncoder = bCryptPasswordEncoder;
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

    private void sendResetEmailForSuperAdmin(String email, String token) {
        // Construct and send the reset password email with a link containing the token
        String resetLink = "http://localhost:1111/reset-password?token=" + token;

        String emailSubject = "Super Admin Created - Pro Collab";
        String emailContent = "<html><body>" +
                "<h2>Pro Collab Password Reset</h2>" +
                "<p>Hello,</p>" +
                "<p>This is your Superadmin Credentials Pro Collab account.</p>" +
                "<p>To continue first enter your password, please click the link below:</p>" +
                "<b>Username :" + email +
                "<p><a href=\"" + resetLink + "\"> Create Password</a></p>" +
                "<p>Enter your password here</p>" +
                "<p>Thank you,</p>" +
                "<p>Pro Collab Team</p>" +
                "</body></html>";

        emailService.sendEmail(email, emailSubject, emailContent);
    }

}
