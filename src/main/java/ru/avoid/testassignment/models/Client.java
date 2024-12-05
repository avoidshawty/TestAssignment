package ru.avoid.testassignment.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name")
    @Size(min = 2, max = 50, message = "name isn't valid")
    private String name;

    @Column(name = "short_name")
    @Size(min = 2, max = 20, message = "short_name isn't valid")
    private String shortName;

    @Column(name = "address")
    @Size(min = 2, max = 50, message = "address isn't valid")
    private String address;

    @ManyToOne
    @JoinColumn(name = "legal_form_id", referencedColumnName = "id")
    private LegalForm legalForm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "depositor")
    private List<Deposit> deposits;

    public Client() {

    }

    public Client(String name, String shortName, String address, LegalForm legalForm) {
        this.name = name;
        this.shortName = shortName;
        this.address = address;
        this.legalForm = legalForm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LegalForm getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(LegalForm legalForm) {
        this.legalForm = legalForm;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }
}
