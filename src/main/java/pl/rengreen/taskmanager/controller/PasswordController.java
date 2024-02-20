package pl.rengreen.taskmanager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
   
}
