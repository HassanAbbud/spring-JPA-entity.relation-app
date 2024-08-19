package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Student;
import java.util.Optional;


public interface StudentRepository extends CrudRepository<Student, Long>{
    @Query("SELECT s From Student s LEFT JOIN FETCH s.courses WHERE s.id=?1")
    Optional<Student> findOneWithCourses(Long id);
}
