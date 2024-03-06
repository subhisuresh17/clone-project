package pl.rengreen.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.EmailService;
import pl.rengreen.taskmanager.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private UserService userService;
    private EmailService emailService;
    private CompanyService companyService;

    @Autowired
    public RegisterController(UserService userService, EmailService emailService,CompanyService companyService) {
        this.userService = userService;
        this.emailService = emailService;
        this.companyService=companyService;
    }   

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        List<Company> allCompanies=companyService.getAllCompanies();
        model.addAttribute("companies", allCompanies);

        return "forms/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/register";
        }

        if (userService.isUserEmailPresent(user.getEmail())) {
            model.addAttribute("exist", true);
            return "forms/register";
        }

        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));

        // Save user with verification token
        userService.createUser(user);

        // Send verification email
        String verificationLink = "http://localhost:1111/register/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

        return "views/success";
    }

    @GetMapping("/register/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        User user = userService.getUserByVerificationToken(token);
        if (user != null && user.getVerificationToken().equals(token) && !userService.isTokenExpired(user)) {
            // Mark user as verified
            user.setVerificationToken(null);
            user.setTokenExpiryDate(null);
            userService.saveUser(user);
            return "views/verifiedEmail";
        } else {
            return "redirect:/register/invalid-token"; // Redirect to an error page
        }
    }

    @GetMapping("/register/verify-email")
    public String verfiyEmailFirst() {
        return "views/verifyEmailFirst";
    }

    @GetMapping("/register/invalid-token")
    public String invalid_token() {
        return "views/invalid_token";
    }

}
