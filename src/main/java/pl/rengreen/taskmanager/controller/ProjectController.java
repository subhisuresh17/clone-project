package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.Project;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.ProjectService;
import pl.rengreen.taskmanager.service.UserService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @PostMapping("/createProject")
    public ResponseEntity<Map<String, String>> addProject(Principal principal, @RequestBody Project project) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        Company company = user.getCompany();
        project.setCreator(user);
        project.setCompany(company);
        Project savedProject = projectService.createProject(project);
        addEmployeeToProject(savedProject.getId(), user.getId());

        // Create a response map with a success message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Project created successfully");

        // Return ResponseEntity with the response map and HTTP status OK
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{projectId}/employees/{userId}")
    public ResponseEntity<Void> addEmployeeToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        projectService.addEmployeeToProject(projectId, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}/employees/{userId}")
    public ResponseEntity<Void> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        projectService.removeEmployeeFromProject(projectId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/employees")
    public ResponseEntity<List<User>> getAllProjectEmployees(@PathVariable Long projectId) {
        List<User> employees = projectService.getAllProjectEmployees(projectId);
        return ResponseEntity.ok(employees);
    }

    // Add other endpoints as needed
}
