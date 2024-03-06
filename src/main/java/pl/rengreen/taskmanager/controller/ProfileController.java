package pl.rengreen.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.TaskDto;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.TaskService;
import pl.rengreen.taskmanager.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private TaskService taskService;

    @Autowired
    public ProfileController(UserService userService, TaskService taskService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        String photoUrl = user.getPhoto();
        photoUrl = photoUrl.replace("\"", ""); // Remove double quotes
        model.addAttribute("photoUrl", photoUrl);
        model.addAttribute("tasksOwned", taskService.findByOwnerOrderByDateDesc(user));
        return "views/profile";
    }

    @GetMapping("/profile/mark-done/{taskId}")
    public String setTaskCompleted(@PathVariable Long taskId) {
        taskService.setTaskCompleted(taskId);
        return "redirect:/profile";
    }

    @GetMapping("/profile/unmark-done/{taskId}")
    public String setTaskNotCompleted(@PathVariable Long taskId) {
        taskService.setTaskNotCompleted(taskId);
        return "redirect:/profile";
    }

    @PostMapping("/profile/profilePicUpdate")
    public ResponseEntity<String> updateProfilePicture(Principal principal, @RequestBody String picUrl) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        picUrl = picUrl.replace("\"", ""); // Replace quotes with an empty string
        userService.updateProfilePic(user, picUrl);
        return ResponseEntity.ok("Profile picture updated successfully");
    }

    @GetMapping("/profileUrl")
    public ResponseEntity<String> profilePictureUrl(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        String photoUrl = user.getPhoto();
        model.addAttribute("photoUrl", photoUrl);
        return ResponseEntity.ok(photoUrl);
    }

}
