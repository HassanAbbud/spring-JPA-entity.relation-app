package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Client;


public interface ClientRepository extends CrudRepository<Client, Long>{

    // Finds by ID and returns ID and Addresses
    @Query("SELECT c FROM Client c JOIN FETCH c.addresses")
    Optional<Client> findOne(Long id);
}
