package david.td.taskmanager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "company")
    private List<Employee> employees = new ArrayList<>();
    @OneToMany(mappedBy = "company")
    private List<Project> projects = new ArrayList<>();

    public List<Project> getProjects() {
        return projects;
    }

    public Long getId() {
        return id;
    }



}
