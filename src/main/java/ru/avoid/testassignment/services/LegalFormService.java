package ru.avoid.testassignment.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.LegalForm;
import ru.avoid.testassignment.repositories.LegalFormRepository;
import ru.avoid.testassignment.util.ClientNotFoundException;
import ru.avoid.testassignment.util.LegalFormNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LegalFormService {

    private final LegalFormRepository legalFormRepository;

    public LegalFormService(LegalFormRepository legalFormRepository) {
        this.legalFormRepository = legalFormRepository;
    }

    public List<LegalForm> findAll() {
        return legalFormRepository.findAll();
    }

    public LegalForm findOne(String name) {
        Optional<LegalForm> foundLegalForm = legalFormRepository.findByLegalForm(name);
        return foundLegalForm.orElseThrow(LegalFormNotFoundException::new);
    }




}
