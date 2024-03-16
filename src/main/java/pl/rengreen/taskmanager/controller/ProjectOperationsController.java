package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.Project;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.ProjectService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
@RequestMapping("/projects")
public class ProjectOperationsController {

    private final ProjectService projectService;
    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public ProjectOperationsController(ProjectService projectService, UserService userService,
            CompanyService companyService) {
        this.projectService = projectService;
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/createProject")
    public String createProject() {
        return "forms/CreateProject";
    }

    @GetMapping("/projectList")
    public String showProjects(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Project> projects = projectService.findByCompanyId(user.getCompany().getId());
        List<Project> userProjects = userService.getUserProjects(user);
        model.addAttribute("projects", projects);
        model.addAttribute("userProjects", userProjects);
        return "views/projects";
    }

    @PostMapping("/markComplete/{projectId}")
    public String markComplete(@PathVariable("projectId") long projectId) {
        Project project = projectService.getProjectById(projectId);

        // Toggle project completion status
        boolean isCompleted = project.isCompleted();
        project.setCompleted(!isCompleted);
        projectService.updateProject(project); // Assuming you have an update method in service

        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/{projectId}")
    public String showProjectDetails(Model model, @PathVariable Long projectId, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        Project project = projectService.getProjectById(projectId);
        List<User> availableUsers = companyService.getCompanyUsers(user.getCompany().getId());
        List<User> projectEmployees = projectService.getAllProjectEmployees(projectId);
        for (User employee : projectEmployees) {
            if (availableUsers.contains(employee)) {
                availableUsers.remove(employee);
            }
        }
        model.addAttribute("project", project);
        model.addAttribute("availableUsers", availableUsers);
        model.addAttribute("projectEmployees", projectEmployees);
        model.addAttribute("projectId", projectId); // Pass the projectId to the view
        return "views/projectDetails";
    }

    @GetMapping("/employees/{projectId}")
    public String viewProjectEmployees(@PathVariable long projectId, Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        Project project = projectService.getProjectById(projectId);
        List<User> availableUsers = companyService.getCompanyUsers(user.getCompany().getId());
        List<User> projectEmployees = projectService.getAllProjectEmployees(projectId);
        for (User employee : projectEmployees) {
            if (availableUsers.contains(employee)) {
                availableUsers.remove(employee);
            }
        }
        model.addAttribute("project", project);
        model.addAttribute("availableUsers", availableUsers);
        return "views/projectEmployees";
    }

    @GetMapping("/tasks/{projectId}")
    public String viewProjectTasks(@PathVariable long projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        return "views/projectTasks";
    }

    @GetMapping("/yourProjects")
    public String getYourProjects(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Project> usersProjectList = user.getProjects();
        model.addAttribute("projects", usersProjectList);
        return "views/yourProjects";
    }

}
