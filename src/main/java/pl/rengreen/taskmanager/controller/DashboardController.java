package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.rengreen.taskmanager.dataloader.DashboardData;
import pl.rengreen.taskmanager.model.Project;
import pl.rengreen.taskmanager.model.ProjectDto;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.TaskDto;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.model.UserDto;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.NoteService;
import pl.rengreen.taskmanager.service.ProjectService;
import pl.rengreen.taskmanager.service.TaskService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
public class DashboardController {

    private final TaskService taskService;
    private final UserService userService;
    private final CompanyService companyService;
    private final ProjectService projectService;
    private final NoteService noteService;

    @Autowired
    public DashboardController(TaskService taskService, UserService userService, CompanyService companyService,
            ProjectService projectService, NoteService noteService) {
        this.taskService = taskService;
        this.userService = userService;
        this.companyService = companyService;
        this.projectService = projectService;
        this.noteService = noteService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        // Count total tasks
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        long totalTasks = taskService.countTasks();
        long completedTasks = taskService.countCompletedTasks();
        long remainingTasks = totalTasks - completedTasks;
        double progress = (double) completedTasks / totalTasks * 100;

        long totalProjects = user.getProjects().size();
        model.addAttribute("user", user);

        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("remainingTasks", remainingTasks);
        model.addAttribute("progress", progress);

        // Return the view name
        return "views/dashboard";
    }

    @GetMapping("/dashboard/allData")
    @ResponseBody
    public Map<String, Object> getDashboardAllData(Principal principal) {
        Map<String, Object> dashboardData = new HashMap<>();
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        // Count total tasks
        long totalTasks = user.getTasksOwned().size();
        long completedTasks = user.getTasksCompleted().size();
        long remainingTasks = totalTasks - completedTasks;

        double taskProgress = (double) completedTasks / totalTasks * 100;

        // Count total projects
        long totalProjects = user.getProjects().size();
        long completedProjects = projectService.countCompletedProjects(user);
        long remainingProjects = totalProjects - completedProjects;

        // Count total users in every project
        Map<String, Integer> usersInProjects = new HashMap<>();
        Map<String,Integer> tasksInProjects=new HashMap<>();
        List<Project> projects = user.getProjects();
        for (Project project : projects) {
            String projectId = project.getName();
            int totalUsersInProject = projectService.getAllProjectEmployees(project.getId()).size();
            int tasksInProject=project.getTasks().size();
            usersInProjects.put(projectId, totalUsersInProject);
            tasksInProjects.put(projectId,tasksInProject);
        }

        dashboardData.put("totalTasks", totalTasks);
        dashboardData.put("completedTasks", completedTasks);
        dashboardData.put("remainingTasks", remainingTasks);
        dashboardData.put("taskProgress", taskProgress);
        dashboardData.put("totalProjects", totalProjects);
        dashboardData.put("completedProjects", completedProjects);
        dashboardData.put("remainingProjects", remainingProjects);
        dashboardData.put("usersInProjects", usersInProjects);
        dashboardData.put("tasksInProjects", tasksInProjects);

        return dashboardData;
    }

    @GetMapping("/dashboard/data")
    @ResponseBody
    public DashboardData getDashboardData(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        // Count total tasks
        long totalTasks = user.getTasksOwned().size();
        long completedTasks = user.getTasksCompleted().size();
        long remainingTasks = totalTasks - completedTasks;

        double progress = (double) completedTasks / totalTasks * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        progress = Double.parseDouble(df.format(progress));

        // Create DashboardData object
        DashboardData data = new DashboardData(totalTasks, completedTasks, remainingTasks, progress);

        return data;
    }

    public TaskDto taskMapper(Task task) {
        TaskDto taskDto = new TaskDto(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getDate().toString(),
                task.isCompleted(),
                task.getCreatorName());
        return taskDto;
    }

    @GetMapping("/dashboard/allTasks")
    @ResponseBody
    public List<TaskDto> getAllTasks(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Task> tasks = user.getTasksOwned();
        List<TaskDto> tasksDto = new ArrayList<>();

        for (Task task : tasks) {
            TaskDto taskDto = taskMapper(task);
            tasksDto.add(taskDto);
        }

        return tasksDto;
    }

    @GetMapping("/dashboard/completedTask")
    @ResponseBody
    public List<TaskDto> getCompletedTask(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Task> tasks = user.getTasksOwned();
        List<TaskDto> tasksDto = new ArrayList<>();

        for (Task task : tasks) {
            if (task.isCompleted()) {
                TaskDto taskDto = taskMapper(task);
                tasksDto.add(taskDto);
            }
        }

        return tasksDto;
    }

    @GetMapping("/dashboard/inCompletedTask")
    @ResponseBody
    public List<TaskDto> getInCompletedTask(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Task> tasks = user.getTasksOwned();
        List<TaskDto> tasksDto = new ArrayList<>();

        for (Task task : tasks) {
            if (!task.isCompleted()) {
                TaskDto taskDto = taskMapper(task);
                tasksDto.add(taskDto);
            }
        }

        return tasksDto;
    }

    public ProjectDto projectMapper(Project project) {
        ProjectDto projectDto = new ProjectDto(project.getId(), project.getName(), project.getDescription(),
                project.getCreator().getName(), project.getDueDate(), project.isCompleted());
        return projectDto;
    }

    @GetMapping("/dashboard/projects")
    @ResponseBody
    public List<ProjectDto> getAllProjects(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Project> projects = user.getProjects();
        List<ProjectDto> projectDtos = new ArrayList<>();
        for (Project project : projects) {
            ProjectDto projectDto = projectMapper(project);
            projectDtos.add(projectDto);
        }
        return projectDtos;
    }

    @GetMapping("/dashboard/compeletedProjects")
    @ResponseBody
    public List<ProjectDto> getAllCompletedProjects(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Project> projects = user.getProjects();
        List<ProjectDto> projectDtos = new ArrayList<>();
        for (Project project : projects) {
            if (project.isCompleted()) {
                ProjectDto projectDto = projectMapper(project);

                projectDtos.add(projectDto);
            }

        }
        return projectDtos;
    }

    @GetMapping("/dashboard/inCompleteProject")
    @ResponseBody
    public List<ProjectDto> getAllInCompleteProject(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Project> projects = user.getProjects();
        List<ProjectDto> projectDtos = new ArrayList<>();
        for (Project project : projects) {
            if (project.isCompleted() == false) {
                ProjectDto projectDto = projectMapper(project);
                projectDtos.add(projectDto);
            }
        }
        return projectDtos;
    }

    public UserDto userMapper(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName(), user.getPhoto());
        return userDto;
    }

    public List<UserDto> getAllProjectUsers(long id) {
        List<User> members = projectService.getAllProjectEmployees(id);
        List<UserDto> myMembers = new ArrayList<>();
        for (User user : members) {
            UserDto userDto = userMapper(user);
            myMembers.add(userDto);
        }
        return myMembers;
    }

    @GetMapping("/dashboard/getProjectMembers/{projectId}")
    @ResponseBody
    public List<UserDto> getAllProjectMembers(@PathVariable("projectId") long id) {
        return getAllProjectUsers(id);
    }

    @GetMapping("/dashboard/getAllTeamMembers")
    @ResponseBody
    public Set<UserDto> getAllTeamMembers(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<Project> projects = user.getProjects();
        Set<UserDto> set = new HashSet<>();
        for (Project project : projects) {
            List<UserDto> allusers = getAllProjectUsers(project.getId());
            for (UserDto userDto : allusers) {
                set.add(userDto);
            }
        }
        return set;
    }

}