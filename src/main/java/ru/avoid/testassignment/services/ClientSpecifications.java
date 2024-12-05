package ru.avoid.testassignment.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.LegalForm;

public class ClientSpecifications {
    public static Specification<Client> equalName(String name) {
        if (name == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    public static Specification<Client> equalShortName(String shortName) {
        if (shortName == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("shortName"), shortName);
        };
    }

    public static Specification<Client> equalAddress(String address) {
        if (address == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("address"), address);
        };
    }

    public static Specification<Client> equalLegalForm(String legalForm) {
        if (legalForm == null)
            return null;
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.join("legalForm").get("legalForm"), legalForm);
        };
    }

}
