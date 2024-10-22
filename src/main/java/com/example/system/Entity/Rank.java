package com.example.system.Entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "level")
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "rank", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Employee> employees;

    public Rank(){
        
    }

    public Rank(Long id, String name, String description, Set<Employee> employees) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.employees = employees;
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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    
}

