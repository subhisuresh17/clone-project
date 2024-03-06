package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.EmailService;
import pl.rengreen.taskmanager.service.TaskService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
public class AssigmentController {
    private UserService userService;
    private TaskService taskService;
    private EmailService emailService;
    private CompanyService companyService;

    @Autowired
    public AssigmentController(UserService userService, TaskService taskService, EmailService emailService,CompanyService companyService) {
        this.userService = userService;
        this.taskService = taskService;
        this.emailService = emailService;
        this.companyService = companyService;
    }

    @GetMapping("/assignment")
    public String showAssigmentForm(Principal principal,Model model) {
        String email=principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> allUsers=companyService.getCompanyUsers(user.getCompany().getId());
        model.addAttribute("users", allUsers);
        model.addAttribute("freeTasks", taskService.findFreeTasks());
        return "forms/assignment";
    }

    @GetMapping("/assignment/{userId}")
    public String showUserAssigmentForm(@PathVariable Long userId, Model model,Principal principal) {
        String email=principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> allUsers=companyService.getCompanyUsers(user.getCompany().getId());
        model.addAttribute("selectedUser", userService.getUserById(userId));
        model.addAttribute("users", allUsers);
        model.addAttribute("freeTasks", taskService.findFreeTasks());
        return "forms/assignment";
    }

    @GetMapping("/assignment/assign/{userId}/{taskId}")
    public String assignTaskToUser(@PathVariable Long userId, @PathVariable Long taskId) {
        Task selectedTask = taskService.getTaskById(taskId);
        User selectedUser = userService.getUserById(userId);
        taskService.assignTaskToUser(selectedTask, selectedUser);
        emailService.sendTaskMail(selectedUser.getEmail(), selectedTask);
        return "redirect:/assignment/" + userId;
    }

    @GetMapping("assignment/unassign/{userId}/{taskId}")
    public String unassignTaskFromUser(@PathVariable Long userId, @PathVariable Long taskId) {
        Task selectedTask = taskService.getTaskById(taskId);
        taskService.unassignTask(selectedTask);
        return "redirect:/assignment/" + userId;
    }

}
