package ru.avoid.testassignment.repositories;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.LegalForm;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {
    //VALIDATION
    Optional<Client> findByName(String name);
    List<Client> findAll();
}
