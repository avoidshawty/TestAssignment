package ru.avoid.testassignment.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "banks")
public class Bank {

    @Id
    @Column(name = "bank_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "bank_name")
    @Size(min = 2, max = 50, message = "name isn't valid")
    private String bankName;

    @Column(name = "bank_code")
    @Size(min = 9, max = 9, message = "bankCode isn't valid")
    private String bankCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bankDeposit")
    private List<Deposit> deposits;

    public Bank() {

    }

    public Bank(int id, String bankName, String bankCode) {
        this.id = id;
        this.bankName = bankName;
        this.bankCode = bankCode;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

}
