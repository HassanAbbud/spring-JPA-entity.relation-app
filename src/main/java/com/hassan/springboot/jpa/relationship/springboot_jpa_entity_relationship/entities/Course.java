package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String instructor;
    
    // @ManyToMany
    // private Set<Student> students = new HashSet<>();

    public Course() {
    }

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String teacher) {
        this.instructor = teacher;
    }

    // public Set<Student> getStudents() {
    //     return students;
    // }

    // public void setStudents(Set<Student> students) {
    //     this.students = students;
    // }

    @Override
    public String toString() {
        return "Course [id=" + id 
        + ", name=" + name 
        + ", teacher=" + instructor 
        + "]";
    }
}
