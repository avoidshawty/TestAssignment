package ru.avoid.testassignment.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "deposit")
public class Deposit {
    @Id
    @Column(name = "deposit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client depositor;

    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "bank_id")
    private Bank bankDeposit;

    @Column(name = "opening_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date openingDate;

    @Column(name = "interest")
    private double interest;

    @Column(name = "duration_in_months")
    private int durationInMonths;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getDepositor() {
        return depositor;
    }

    public void setDepositor(Client depositor) {
        this.depositor = depositor;
    }

    public Bank getBankDeposit() {
        return bankDeposit;
    }

    public void setBankDeposit(Bank bankDeposit) {
        this.bankDeposit = bankDeposit;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }
}
