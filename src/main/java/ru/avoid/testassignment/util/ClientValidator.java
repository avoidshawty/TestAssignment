package ru.avoid.testassignment.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.services.ClientsService;

@Component
public class ClientValidator implements Validator {

    private final ClientsService clientsService;

    @Autowired
    public ClientValidator(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Client.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Client client = (Client) o;

//        if (clientsService.getClientByLegalForm(client.getLegalForm()).isPresent())
//            errors.rejectValue("Name", "", "Человек с таким OOO уже существует");
    }
}
