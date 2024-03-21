package pl.rengreen.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.TaskHistory;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.TaskHistoryRepository;
import pl.rengreen.taskmanager.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private TaskHistoryService taskHistoryService;
    private TaskHistoryRepository taskHistoryRepository;
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,TaskHistoryService taskHistoryService,TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository=taskHistoryRepository;
        this.taskHistoryService=taskHistoryService;
    }

    @Override
    public void createTask(Task task) {
        task.setAction("UnAssigned");
        taskRepository.save(task);
        LocalDateTime date=LocalDateTime.now();
        TaskHistory taskHistory=new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setAction("Not Assigned");
        taskHistory.setTimestamp(date);;
        taskHistory.setUser(task.getOwner());
        taskHistoryRepository.save(taskHistory);
    }

    @Override
    public void updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.getOne(id);
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setDate(updatedTask.getDate());
        taskRepository.save(task);
        LocalDateTime date=LocalDateTime.now();
        TaskHistory taskHistory=new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setAction("Not Assigned");
        taskHistory.setTimestamp(date);;
        taskHistory.setUser(task.getOwner());
        taskHistoryRepository.save(taskHistory);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByOwnerOrderByDateDesc(User user) {
        return taskRepository.findByOwnerOrderByDateDesc(user);
    }

    @Override
    public void setTaskCompleted(Long id) {
        Task task = taskRepository.getOne(id);
        task.setCompleted(true);
        taskRepository.save(task);
    }

    @Override
    public void setTaskNotCompleted(Long id) {
        Task task = taskRepository.getOne(id);
        task.setCompleted(false);
        taskRepository.save(task);
    }

    @Override
    public List<Task> findFreeTasks() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getOwner() == null && !task.isCompleted())
                .collect(Collectors.toList());

    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public void assignTaskToUser(Task task, User user) {
        task.setOwner(user);
        task.setAction("Assigned");
        taskRepository.save(task);
        LocalDateTime date=LocalDateTime.now();
        TaskHistory taskHistory=new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setAction("Assigned");
        taskHistory.setTimestamp(date);;
        taskHistory.setUser(task.getOwner());
        taskHistoryRepository.save(taskHistory);
    }

    @Override
    public void unassignTask(Task task) {
        task.setOwner(null);
        task.setAction("Unassigned");
        taskRepository.save(task);
        LocalDateTime date=LocalDateTime.now();
        TaskHistory taskHistory=new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setAction("Unassigned From");
        taskHistory.setTimestamp(date);
        taskHistory.setUser(task.getOwner());
        taskHistoryRepository.save(taskHistory);
    }

    public long countTasks() {
        return taskRepository.count();
    }

    @Override
    public long countCompletedTasks() {
        return taskRepository.countByIsCompleted(true);
    }
}
