package ru.avoid.testassignment.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "legalforms")
public class LegalForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "legal_form")
    private String legalForm;

    @OneToMany(mappedBy = "legalForm")
    private List<Client> clients;

    public LegalForm() {

    }

    public LegalForm(int id, String legalForm, List<Client> clients) {
        this.id = id;
        this.legalForm = legalForm;
        this.clients = clients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public String getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }
}
