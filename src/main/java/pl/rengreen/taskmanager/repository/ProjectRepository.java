package pl.rengreen.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
