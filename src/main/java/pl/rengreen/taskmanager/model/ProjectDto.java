package pl.rengreen.taskmanager.model;

import java.time.LocalDate;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ProjectDto {
    private Long projectId;
    private String name;
    private String description;
    private String creatorName;
    private LocalDate dueDate;
    private boolean isCompleted;

    public ProjectDto(Long projectId, String name, String description, String creatorName, LocalDate dueDate,
            boolean isCompleted) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.creatorName = creatorName;
        this.dueDate = dueDate;
        this.isCompleted = isCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

}
