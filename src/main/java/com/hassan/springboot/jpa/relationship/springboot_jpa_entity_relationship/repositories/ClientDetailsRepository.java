package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories;

import org.springframework.data.repository.CrudRepository;

import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.ClientDetails;

public interface ClientDetailsRepository extends CrudRepository<ClientDetails, Long>{
    

}
