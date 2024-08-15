package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Client;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Invoice;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.ClientRepository;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.InvoiceRepository;

@SpringBootApplication
public class SpringbootJpaEntityRelationshipApplication implements CommandLineRunner {
	
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository; 

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaEntityRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		manyToOne();
	}

	public void manyToOne(){
		Client client = new Client("Flush", "Flusherson");
		clientRepository.save(client);

		Invoice invoice = new Invoice("Office expenses", 2000L);
		invoice.setClient(client);
		invoiceRepository.save(invoice); 
	}

}
