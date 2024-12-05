package ru.avoid.testassignment.services;

import org.springframework.data.jpa.domain.Specification;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.Deposit;

import java.util.Date;

public class DepositsSpecifications {

    public static Specification<Deposit> equalInterest(double interest) {
        if (interest == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("interest"), interest);
        };
    }

    public static Specification<Deposit> equalDurationInMonths(int durationInMonths) {
        if (durationInMonths == 0)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("durationInMonths"), durationInMonths);
        };
    }

    public static Specification<Deposit> equalClientName(String clientName) {
        if (clientName == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.join("depositor").get("name"), clientName);
        };
    }

    public static Specification<Deposit> equalBankName(String bankName) {
        if (bankName == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.join("bankDeposit").get("bankName"), bankName);
        };
    }

}
