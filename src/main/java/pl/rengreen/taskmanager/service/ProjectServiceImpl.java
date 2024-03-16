package pl.rengreen.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import pl.rengreen.taskmanager.model.Project;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public void addEmployeeToProject(Long projectId, User user) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if (project.getEmployees() == null) {
                project.setEmployees(new ArrayList<>());
            }
            project.getEmployees().add(user);
            projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }
    }

    @Override
    public void removeEmployeeFromProject(Long projectId, User user) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if (project.getEmployees() != null) {
                project.getEmployees().remove(user);
                projectRepository.save(project);
            }
        } else {
            throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }
    }

    @Override
    public List<User> getAllProjectEmployees(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            return project.getEmployees();
        } else {
            throw new IllegalArgumentException("Project not found with ID: " + projectId);
        }
    }

    @Override
    public List<Project> findByCompanyId(Long companyId) {
        return projectRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean isUserPresentInProject(Project project, User user) {
        // TODO Auto-generated method stub
        if (project == null) {
            return true;
        }
        Project project2 = projectRepository.findById(project.getId()).get();
        List<User> users = project2.getEmployees();
        if (users.contains(user)) {
            return true;
        }
        return false;
    }

    @Override
    public long countCompletedProjects(User user) {
        return projectRepository.countByCreatorAndIsCompletedTrue(user);
    }
}
