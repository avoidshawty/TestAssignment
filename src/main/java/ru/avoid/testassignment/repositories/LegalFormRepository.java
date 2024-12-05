package ru.avoid.testassignment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.avoid.testassignment.models.LegalForm;

import java.util.Optional;

@Repository
public interface LegalFormRepository extends JpaRepository<LegalForm, Integer> {
        public Optional<LegalForm> findByLegalForm(String legalFormName);
}
