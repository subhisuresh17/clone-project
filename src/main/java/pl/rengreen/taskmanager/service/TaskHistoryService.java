package pl.rengreen.taskmanager.service;

import java.util.List;

import pl.rengreen.taskmanager.model.TaskHistory;

public interface TaskHistoryService {
    TaskHistory save(TaskHistory taskHistory);
    List<TaskHistory> findByTaskId(Long taskId);
}
