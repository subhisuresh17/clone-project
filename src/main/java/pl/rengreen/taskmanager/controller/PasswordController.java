package pl.rengreen.taskmanager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.TaskService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
public class PasswordController {

    UserService userService;
    TaskService taskService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordController(UserService userService,TaskService taskService) {
        this.userService = userService;
        this.taskService=taskService;
    }

    @GetMapping("/profile/changePassword")
    public String changePassword() {
        return "views/changePassword";
    }
    @PostMapping("/profile/changeUserPassword")
    public String changeUserPassword(@RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Principal principal,
                                     Model model) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        
        // Check if the old password matches the one stored for the user
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("error", "Old password is incorrect");
            return "redirect:/changePassword";
        }
        
        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirm password do not match");
            return "redirect:/changePassword"; // Redirect to the change password page with error message
        }
        
        // Update the password in the database
        userService.updatePassword(user, passwordEncoder.encode(newPassword));
        return "redirect:/login?Changed_Password"; 
    }
    @PostMapping("/profile/checkOldPassword")
    public ResponseEntity<?> checkOldPassword(@RequestParam("oldPassword") String oldPassword, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        
        // Check if the old password matches the one stored for the user
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.ok().body("{\"valid\": true}");
        } else {
            return ResponseEntity.ok().body("{\"valid\": false}");
        }
    }
   
}
