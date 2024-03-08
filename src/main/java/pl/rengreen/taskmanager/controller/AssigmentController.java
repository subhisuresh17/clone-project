package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.EmailService;
import pl.rengreen.taskmanager.service.TaskService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
@RequestMapping("/assignment")
public class AssigmentController {
    private UserService userService;
    private TaskService taskService;
    private EmailService emailService;
    private CompanyService companyService;

    @Autowired
    public AssigmentController(UserService userService, TaskService taskService, EmailService emailService,
            CompanyService companyService) {
        this.userService = userService;
        this.taskService = taskService;
        this.emailService = emailService;
        this.companyService = companyService;
    }

    public List<Task> freeTasks(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Task> task = taskService.findFreeTasks();
        List<Task> companyTask = new ArrayList<>();
        long company_id = user.getCompany().getId();
        for (Task t : task) {
            if (companyService.isUserPresentInCompany(t.getCreatedUser(), company_id)) {
                companyTask.add(t);
            }
        }
        return companyTask;
    }

    @GetMapping
    public String showAssigmentForm(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> allUsers = companyService.getCompanyUsers(user.getCompany().getId());
        List<Task> freeTasks = freeTasks(principal);
        model.addAttribute("users", allUsers);
        model.addAttribute("freeTasks", freeTasks);
        return "forms/assignment";
    }

    @GetMapping("/{userId}")
    public String showUserAssigmentForm(@PathVariable Long userId, Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> allUsers = companyService.getCompanyUsers(user.getCompany().getId());
        List<Task> freeTasks = freeTasks(principal);
        model.addAttribute("selectedUser", userService.getUserById(userId));
        model.addAttribute("users", allUsers);
        model.addAttribute("freeTasks", freeTasks);
        return "forms/assignment";
    }

    @GetMapping("/assign/{userId}/{taskId}")
    public String assignTaskToUser(@PathVariable Long userId, @PathVariable Long taskId) {
        Task selectedTask = taskService.getTaskById(taskId);
        User selectedUser = userService.getUserById(userId);
        taskService.assignTaskToUser(selectedTask, selectedUser);
        emailService.sendTaskMail(selectedUser.getEmail(), selectedTask);
        return "redirect:/assignment/" + userId;
    }

    @GetMapping("unassign/{userId}/{taskId}")
    public String unassignTaskFromUser(@PathVariable Long userId, @PathVariable Long taskId) {
        Task selectedTask = taskService.getTaskById(taskId);
        taskService.unassignTask(selectedTask);
        return "redirect:/assignment/" + userId;
    }

}
