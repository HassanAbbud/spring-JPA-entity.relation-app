package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Address;
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
		oneToManyFindById();
	}

	// Create client and assign its ID to an Invoice
	@Transactional
	public void manyToOneCreate(){
		Client client = new Client("Flush", "Flusherson");
		clientRepository.save(client);

		Invoice invoice = new Invoice("Office expenses", 2000L);
		invoice.setClient(client);
		invoiceRepository.save(invoice); 
		System.out.println(invoice);
	}
	
	// Find a client and assign its ID to an Invoice
	@Transactional
	public void manyToOneFindClientById(){
		Optional<Client> optionalClient = clientRepository.findById(1L);

		if(optionalClient.isPresent()){
			Client client = optionalClient.orElseThrow();
			clientRepository.save(client);
	
			Invoice invoice = new Invoice("Office expenses", 2000L);
			invoice.setClient(client);
			invoiceRepository.save(invoice); 
			System.out.println(invoice);
		}
	}

	// Assign addresses to a new client
	@Transactional
	public void oneToMany(){
		Client client =  new Client("Mike", "Smith");
		
		Address address1 = new Address("Sir. Street Dr.", 4312);
		Address address2 = new Address("Mr. Road Dr.", 1234);
		
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		//save to db 
		clientRepository.save(client);

		System.out.println(client);
	}

	//Assign addresses to an existing client by ID
	@Transactional
	public void oneToManyFindById(){
		Optional<Client> optionalClient = clientRepository.findById(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("Sir. Street Dr.", 4312);
			Address address2 = new Address("Mr. Road Dr.", 1234);
			//This way ensures theres no failed to lazy init. Directly assign during transaction
			client.setAddresses(Arrays.asList(address1, address2));
	
			//save to db 
			clientRepository.save(client);
	
			System.out.println(client);
		});
	}
}
