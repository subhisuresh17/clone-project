package pl.rengreen.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.rengreen.taskmanager.model.Project;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.ProjectService;
import pl.rengreen.taskmanager.service.TaskService;
import pl.rengreen.taskmanager.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    private CompanyService companyService;
    private ProjectService projectService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, CompanyService companyService,
            ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.companyService = companyService;
        this.projectService = projectService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
        List<User> allUsers = companyService.getCompanyUsers(signedUser.getCompany().getId());
        List<Task> allTask = companyService.getAllTaskByCompany(signedUser.getCompany().getId());
        model.addAttribute("tasks", allTask);
        model.addAttribute("users", allUsers);
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/task/create")
    public String showEmptyTaskForm(Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Task task = new Task();
        task.setCreatorName(user.getName());
        task.setCreatedUser(user);
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }

    @GetMapping("/task/create/{projectId}")
    public String createTaskForProject(@PathVariable long projectId, Model model, Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Task task = new Task();
        task.setCreatorName(user.getName());
        task.setCreatedUser(user);
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }

    @PostMapping("/task/create/{projectId}")
    public String createTaskForProject(@PathVariable long projectId,
            @Valid Task task,
            BindingResult bindingResult,
            Model model,
            Principal principal,
            SecurityContextHolderAwareRequestWrapper request) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }

        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        // Set creator, owner, and created user
        task.setCreatorName(user.getName());
        task.setCreatedUser(user);
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }

        // Set the project for the task
        Project project = projectService.getProjectById(projectId);
        task.setProject(project);

        // Save the task
        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @PostMapping("/task/create")
    public String createTask(@Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }

        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "forms/task-edit";
    }

    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid Task task, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }

}
