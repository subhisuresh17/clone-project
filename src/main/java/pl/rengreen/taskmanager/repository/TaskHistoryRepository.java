package pl.rengreen.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.TaskHistory;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory,Long>{

    List<TaskHistory> findByTaskId(Long taskId);
    
}
