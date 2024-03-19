package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pl.rengreen.taskmanager.model.AnswerMessage;
import pl.rengreen.taskmanager.model.CallMessage;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.model.UserDto;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
public class videoCallController {

    @Autowired
    private final UserService userService;
    private final CompanyService companyService;

    public videoCallController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @MessageMapping("/call/{userId}")
    @SendTo("/topic/call/{userId}")
    public CallMessage callUser(@DestinationVariable Long userId, CallMessage message) {
        // Handle incoming call and send offer to user with userId
        return message;
    }

    @MessageMapping("/answer/{userId}")
    public void answerCall(@DestinationVariable Long userId, AnswerMessage message) {
        // Handle answer from user with userId
    }

    public UserDto userMapper(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName(), user.getPhoto());
        return userDto;
    }

    @GetMapping("/video/call")
    public String videoCall(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> employees = companyService.getCompanyUsers(user.getCompany().getId());
        List<UserDto> allEmployee = new ArrayList<>();
        for (User e : employees) {
            allEmployee.add(userMapper(e));
        }
        model.addAttribute("Users", allEmployee);
        model.addAttribute("signedIn", user);
        return "/chat/VideoCall";
    }
}