package pl.rengreen.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCompanyId(Long companyId);

}
