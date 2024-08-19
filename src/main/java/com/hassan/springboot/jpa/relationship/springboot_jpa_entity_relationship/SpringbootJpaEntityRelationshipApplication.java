package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Address;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Client;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.ClientDetails;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Course;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Invoice;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities.Student;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.ClientDetailsRepository;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.ClientRepository;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.CourseRepository;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.InvoiceRepository;
import com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.repositories.StudentRepository;

@SpringBootApplication
public class SpringbootJpaEntityRelationshipApplication implements CommandLineRunner {
	
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository; 

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaEntityRelationshipApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		manyToManyBidirectionalFind();
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
		Optional<Client> optionalClient = clientRepository.findOne(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("Sir. Street Dr.", 4312);
			Address address2 = new Address("Mr. Road Dr.", 1234);
			//This way ensures theres no failed to lazy init. Directly assign during transaction
			//get method will throw error because client only had id and will not know attribute address
			Set<Address> addresses = new HashSet<>();
			addresses.add(address1);
			addresses.add(address2);
			client.setAddresses(addresses);
	
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
		
		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);
		client.setAddresses(addresses);

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
		//This only consults the ID and not other attributes (lazy)
		Optional<Client> optionalClient = clientRepository.findById(2L);

		optionalClient.ifPresent(client -> {
			Address address1 = new Address("Sir. Street Dr.", 4312);
			Address address2 = new Address("Mr. Road Dr.", 1234);
			//This way ensures theres no failed to lazy init. Directly assign during transaction
			//get method will throw error because client only had id and will not know attribute address
			Set<Address> addresses = new HashSet<>();
			addresses.add(address1);
			addresses.add(address2);
			client.setAddresses(addresses);
	
			//save to db 
			clientRepository.save(client);
	
			System.out.println(client);
			Optional<Client> optionalClient2 = clientRepository.findOneWithAddresses(2L);
	
			optionalClient2.ifPresent(selectedClient -> {
				selectedClient.getAddresses().remove(address1);
	
				clientRepository.save(selectedClient);
				System.out.println(selectedClient);
			});
		});
	}

	// create and set bidirectional Client with invoices containing client IDs
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

	// Find Client by ID and add invoices containing its client IDs
	@Transactional 
	public void oneToManyInvoiceBidirectionalFindById(){
		// Fetched with both invoices and addresses to print result and avoid LazyInitializationException 
		Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent(client -> {
	
			Invoice invoice1 = new Invoice("House electricity", 200L);
			Invoice invoice2 = new Invoice("Water Service", 500L);
	
			client.addInvoice(invoice1).addInvoice(invoice2);
	
			clientRepository.save(client);
	
			System.out.println(client);
		});
	}

	@Transactional
	public void removeInvoiceBidirectionalFindById() {
		Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent(client -> {

			Invoice invoice1 = new Invoice("House expenses", 5000L);
			Invoice invoice2 = new Invoice("Office expenses", 8000L);

			client.addInvoice(invoice1).addInvoice(invoice2);

			clientRepository.save(client);

			System.out.println(client);
		});

		Optional<Client> optionalClientDb = clientRepository.findOne(1L);

		optionalClientDb.ifPresent(client -> {
			// Invoice invoice3 = new Invoice("House expenses", 5000L);
			// invoice3.setId(1L);

			Optional<Invoice> invoiceOptional = invoiceRepository.findById(1L);
			invoiceOptional.ifPresent(invoice -> {
				client.removeInvoice(invoice);
				clientRepository.save(client);
				System.out.println(client);
			});
		});
	}

	@Transactional
	public void oneToOne() {
		ClientDetails clientDetails = new ClientDetails(true, 5000);
		clientDetailsRepository.save(clientDetails);
		
		Client client = new Client("Joseph", "Joestar");
		client.setClientDetails(clientDetails);
		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void oneToOneBidirectional(){
		Client client = new Client("Chris", "Christian");

		ClientDetails clientDetails = new ClientDetails(true, 2000);

		client.setClientDetails(clientDetails);
		clientDetails.setClient(client);

		clientRepository.save(client);

		System.out.println(client);
	}

	@Transactional
	public void oneToOneBidirectionalFindById(){
		Optional<Client> optionalClient = clientRepository.findOne(2L);

		optionalClient.ifPresent(client -> {
			ClientDetails clientDetails = new ClientDetails(true, 2000);
			client.setClientDetails(clientDetails);
			clientDetails.setClient(client);
			
			clientRepository.save(client);
	
			System.out.println(client);
		});
	}

	@Transactional
	public void manyToMany(){
		Student student1 = new Student("Jose", "Loya");
		Student student2 = new Student("Santiago", "Arzaga");

		Course course1 = new Course("Design of minimal systems", "Raime");
		Course course2 = new Course("Introduction to cybersecurity", "George");

		student1.setCourses(Set.of(course1, course2));
		student2.setCourses(Set.of(course1));
		
		studentRepository.saveAll(List.of(student1,student2));

		System.out.println(student1);
		System.out.println(student2);
	}
	
	@Transactional
	public void manyToManyFindById(){
		Optional<Student> optionalStudent =  studentRepository.findById(2L);

		optionalStudent.ifPresent(student -> {
			Course course1 = new Course("Design of minimal systems", "Raime");
			Course course2 = new Course("Introduction to cybersecurity", "George");
			
			student.setCourses(Set.of(course1, course2));
			studentRepository.save(student);

			System.out.println(student);
		});
	}
	
	@Transactional
	public void manyToManyRemoveFind(){
		Optional<Student> optionalStudent =  studentRepository.findOneWithCourses(2L);

		optionalStudent.ifPresent(student -> {
			Course course1 = new Course("Design of minimal systems", "Raime");
			Course course2 = new Course("Introduction to cybersecurity", "George");
			
			student.setCourses(Set.of(course1, course2));
			studentRepository.save(student);

			System.out.println(student);
		});

		Optional<Student> optionalStudentDb =  studentRepository.findOneWithCourses(2L);
		optionalStudentDb.ifPresent(studentDb -> {
			Optional<Course> courseOptionalDb = courseRepository.findById(1L);

			courseOptionalDb.ifPresent(courseDb -> {
				studentDb.getCourses().remove(courseDb);

				studentRepository.save(studentDb);

				System.out.println(studentDb);
			});
		});
	}

	@Transactional
	public void manyToManyBidirectionalFind(){
		Optional<Student> optionalStudent1 = studentRepository.findOneWithCourses(1L);
		Optional<Student> optionalStudent2 = studentRepository.findOneWithCourses(2L);

		if (optionalStudent1.isPresent() && optionalStudent2.isPresent()) {
			Student student1 = optionalStudent1.get();	
			Student student2 = optionalStudent2.get();
			
			Optional<Course> optionalCourse1 = courseRepository.findOneWithStudents(1L);
			Optional<Course> optionalCourse2 = courseRepository.findOneWithStudents(2L);
			
			Course course1 = optionalCourse1.get();
			Course course2 = optionalCourse2.get();
			
			student1.addCourse(course1);
			student1.addCourse(course2);
			student2.addCourse(course2);

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);
		}
	}
}
