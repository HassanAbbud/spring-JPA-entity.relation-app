package com.hassan.springboot.jpa.relationship.springboot_jpa_entity_relationship.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name="last_name")
    private String lastName;

    // One client has many addresses
    // @JoinColumn(name = "client_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
        name = "tbl_clients_to_addresses", 
        joinColumns = @JoinColumn(name= "client_id"),
        inverseJoinColumns = @JoinColumn(name="address_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"address_id"})
    )
    private List<Address> addresses = new ArrayList<>();

    // inverse relationship with invoice
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    List<Invoice> invoices = new ArrayList<>();

    public Client() {
    }

    public Client(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    // Assign an invoice to this client entity
    public void addInvoice(Invoice invoice){
        invoices.add(invoice);
        invoice.setClient(this);
    }

    @Override
    public String toString() {
        return "Client [id=" + id 
        + ", name=" + name 
        + ", lastName=" + lastName 
        + ", addresses=" + addresses
        + ", invoices=" + invoices 
        + "]";
    }
}
