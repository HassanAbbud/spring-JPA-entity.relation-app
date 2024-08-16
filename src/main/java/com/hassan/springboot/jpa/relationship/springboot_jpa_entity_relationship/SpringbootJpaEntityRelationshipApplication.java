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
		oneToManyInvoiceBidirectional();
		// removeClientById(3L);
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

	// Delete client by ID invoices and addresses relation in client will be cascaded (deleted)
	@Transactional
    public void removeClientById(Long clientId) {
        clientRepository.deleteById(clientId);
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
		//This only consults the ID and not other attributes (lazy)
		Optional<Client> optionalClient = clientRepository.findById(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("Sir. Street Dr.", 4312);
			Address address2 = new Address("Mr. Road Dr.", 1234);
			//This way ensures theres no failed to lazy init. Directly assign during transaction
			//get method will throw error because client only had id and will not know attribute address
			client.setAddresses(Arrays.asList(address1, address2));
	
			//save to db 
			clientRepository.save(client);
	
			System.out.println(client);
		});
	}
	
	// Remove address by ID ( app.properties spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true)
	@Transactional
	public void removeAddress(){
		Client client =  new Client("Mike", "Smith");
		
		Address address1 = new Address("Sir. Street Dr.", 4312);
		Address address2 = new Address("Mr. Road Dr.", 1234);
		
		client.setAddresses(Arrays.asList(address1, address2));

		//save to db 
		clientRepository.save(client);

		System.out.println(client);

		Optional<Client> optionalClient = clientRepository.findById(3L);

		optionalClient.ifPresent(selectedClient -> {
			//This way ensures theres no failed to lazy init. Directly assign during transaction
			selectedClient.getAddresses().remove(address1);

			clientRepository.save(selectedClient);
			System.out.println(client);
		});
	}

	@Transactional
	public void removeAddressByCustomQuery(){
		oneToManyFindById();
		Optional<Client> optionalClient2 = clientRepository.findOne(2L);

		optionalClient2.ifPresent(selectedClient -> {
			selectedClient.getAddresses().remove(0);

			clientRepository.save(selectedClient);
			System.out.println(selectedClient);
		});
	}

	@Transactional
	public void oneToManyInvoiceBidirectional(){
		Client client = new Client("Alex", "River");

		Invoice invoice1 = new Invoice("House rent", 2000L);
		Invoice invoice2 = new Invoice("Water service", 500L);

		//Set bidirectional attributes on client and invoice
		client.addInvoice(invoice1);
		client.addInvoice(invoice2);

		clientRepository.save(client);

		System.out.println(client);

	}
}
