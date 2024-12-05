package ru.avoid.testassignment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.Deposit;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer>, JpaSpecificationExecutor<Deposit> {
    Optional<Deposit> findById(int id);
}
