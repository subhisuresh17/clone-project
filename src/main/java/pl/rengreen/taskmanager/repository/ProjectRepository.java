package pl.rengreen.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.Project;
import pl.rengreen.taskmanager.model.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCompanyId(Long companyId);

    long countByCreatorAndIsCompletedTrue(User user);

}
