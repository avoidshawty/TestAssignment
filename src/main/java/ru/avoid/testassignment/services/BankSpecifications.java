package ru.avoid.testassignment.services;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import ru.avoid.testassignment.models.Bank;
import ru.avoid.testassignment.models.Client;


public class BankSpecifications {
    public static Specification<Bank> equalBankName(String bankName) {
        if (bankName == null){
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("bankName"), bankName);
        };
    }

    public static Specification<Bank> equalBankCode(String bankCode) {
        if (bankCode == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("bankCode"), bankCode);
        };
    }
}
