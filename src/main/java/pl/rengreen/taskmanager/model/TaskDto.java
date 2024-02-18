package pl.rengreen.taskmanager.model;

public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private String date;
    private boolean isCompleted;
    private String creatorName;

    public TaskDto(Long id, String name, String description, String date, boolean isCompleted, String creatorName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.isCompleted = isCompleted;
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

}