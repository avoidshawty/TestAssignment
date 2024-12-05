package ru.avoid.testassignment.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.avoid.testassignment.DTO.ClientDTO;
import ru.avoid.testassignment.services.ClientsService;
import ru.avoid.testassignment.services.LegalFormService;
import ru.avoid.testassignment.util.ClientNotCreatedException;
import ru.avoid.testassignment.util.ClientNotFoundException;
import ru.avoid.testassignment.util.ClientValidator;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    private final ClientsService clientsService;
    private final ModelMapper modelMapper;
    private final ClientValidator clientValidator;
    private final LegalFormService legalFormService;

    @Autowired
    public ClientsController(ClientsService clientsService, ModelMapper modelMapper, ClientValidator clientValidator, LegalFormService legalFormService) {
        this.clientsService = clientsService;
        this.modelMapper = modelMapper;
        this.clientValidator = clientValidator;
        this.legalFormService = legalFormService;
    }

    @GetMapping()
    public List<ClientDTO> getClients(@RequestParam(value = "sort_by_id", required = false, defaultValue = "false") boolean sortById,
                                      @RequestParam(value = "sort_by_name", required = false, defaultValue = "false") boolean sortByName,
                                      @RequestParam(value = "sort_by_short_name", required = false, defaultValue = "false") boolean sortByShortName,
                                      @RequestParam(value = "sort_by_address", required = false, defaultValue = "false") boolean sortByAddress,
                                      @RequestParam(value = "name", required = false, defaultValue = "") String Name,
                                      @RequestParam(value = "short_name", required = false, defaultValue = "") String ShortName,
                                      @RequestParam(value = "address", required = false, defaultValue = "") String Address,
                                      @RequestParam(value = "legal_form", required = false, defaultValue = "") String LegalForm
    ) {
        // SORT BY

        return clientsService.findAll(sortById, sortByName, sortByShortName, sortByAddress,
                        Name, ShortName, Address, LegalForm);

    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable("id") int id) {
        return clientsService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createClient(@RequestBody @Valid ClientDTO clientDTO,
                                                   BindingResult bindingResult) {
        GeneralExceptionHandler.handleValidationErrors(bindingResult, ClientNotCreatedException.class);
        clientsService.save(clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateClient(@PathVariable int id, @RequestBody @Valid ClientDTO clientDTO, BindingResult bindingResult) {
        GeneralExceptionHandler.handleValidationErrors(bindingResult, ClientNotCreatedException.class);
        clientsService.update(id, clientDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable int id) {
        try {
            clientsService.delete(id);
            return ResponseEntity.ok().build();
        } catch (ClientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
