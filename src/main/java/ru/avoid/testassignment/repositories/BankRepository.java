package ru.avoid.testassignment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.avoid.testassignment.models.Bank;
import ru.avoid.testassignment.models.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer>, JpaSpecificationExecutor<Bank> {
    Optional<Bank> findByBankName(String bankName);
}

