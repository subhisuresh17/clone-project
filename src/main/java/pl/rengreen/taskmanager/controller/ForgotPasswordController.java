package pl.rengreen.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.ForgotPasswordService;

@Controller
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    // Display the forgot password form
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forms/forgotPassword";
    }

    // Handle the form submission to initiate password reset
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email) {
        forgotPasswordService.initiatePasswordReset(email);
        return "redirect:/forgot-password-success";
    }
    // @PostMapping("/forgot-password/superAdmin/createSuperAdminPassword")
    // public String createSuperAdmin(@RequestParam("email") String email) {
    // forgotPasswordService.initiateSuperAdmin(email);
    // return "redirect:/superAdmin/superAdminCredentials";
    // }

    @GetMapping("/forgot-password-success")
    public String viewPasswordSuccess() {
        return "forms/forgot-password-success";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Verify the token and display the reset password form if valid
        User userOptional = forgotPasswordService.verifyResetToken(token);
        if (userOptional != null) {
            // Token is valid, add user to the model and return reset password page
            model.addAttribute("user", userOptional);
            return "forms/reset_password"; // Assuming reset_password.html is your reset password page
        } else {
            // Token is invalid or expired, redirect to invalid token page
            return "redirect:/forgot-password/invalid-token";
        }
    }

    @GetMapping("/forgot-password/invalid-token")
    public String invalidToken() {
        return "redirect:/register/invalid-token";
    }

    // Handle the form submission to reset password
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        // Verify the token and reset the password
        forgotPasswordService.resetPassword(token, password);
        return "redirect:/login";
    }
}
