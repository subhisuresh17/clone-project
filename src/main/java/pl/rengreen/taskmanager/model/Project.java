package pl.rengreen.taskmanager.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;
    private String creator_name;
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<User> users = new ArrayList<>();

    public Project() {
    }

    public Project(Long id, String name, String creator_name, LocalDate dueDate, Company company) {
        this.id = id;
        this.name = name;
        this.creator_name = creator_name;
        this.dueDate = dueDate;
        this.company = company;
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

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    // public List<User> getUsers() {
    // return users;
    // }

    // public void setUsers(List<User> users) {
    // this.users = users;
    // }

}
