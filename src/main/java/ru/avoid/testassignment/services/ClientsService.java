package ru.avoid.testassignment.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avoid.testassignment.DTO.ClientDTO;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.LegalForm;
import ru.avoid.testassignment.repositories.ClientRepository;
import ru.avoid.testassignment.util.ClientNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientsService {

    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;

    private final LegalFormService legalFormService;


    @Autowired
    public ClientsService(ClientRepository clientRepository, LegalFormService legalFormService) {
        this.clientRepository = clientRepository;
        this.legalFormService = legalFormService;
        this.modelMapper = new ModelMapper();
    }

    public List<ClientDTO> findAll(boolean sortById, boolean sortByName,
                                   boolean sortByShortName, boolean sortByAddress,
                                   String name, String shortName,
                                   String address, String legalForm) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (sortById) {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        else if (sortByName) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        }

        else if (sortByShortName) {
            sort = Sort.by(Sort.Direction.ASC, "shortName");
        }

        else if (sortByAddress) {
            sort = Sort.by(Sort.Direction.ASC, "address");
        }

        if (name != null && !name.isEmpty()) {
            return clientRepository.findAll(ClientSpecifications.equalName(name), sort)
                    .stream().map(this::convertToClientDTO).toList();
        }
        if (shortName != null && !shortName.isEmpty()) {
            return clientRepository.findAll(ClientSpecifications.equalShortName(shortName), sort)
                    .stream().map(this::convertToClientDTO).toList();
        }

        if (address != null && !address.isEmpty()) {
            return clientRepository.findAll(ClientSpecifications.equalAddress(address), sort)
                    .stream().map(this::convertToClientDTO).toList();
        }

        if (legalForm != null && !legalForm.isEmpty()) {

            return clientRepository.findAll(ClientSpecifications.equalLegalForm(legalForm), sort)
                    .stream().map(this::convertToClientDTO).toList();
        }

        return clientRepository.findAll(sort)
                .stream().map(this::convertToClientDTO).toList();
    }


    public ClientDTO findOne(int id) {
        Optional<Client> foundClient = clientRepository.findById(id) ;
        return convertToClientDTO(foundClient.orElseThrow(ClientNotFoundException::new));
    }

    public Client findOne(String name) {
        Optional<Client> foundClient = clientRepository.findByName(name);
        return foundClient.orElseThrow(ClientNotFoundException::new);
    }

    @Transactional
    public void save(ClientDTO clientDTO) {

        clientRepository.save(convertToClient(clientDTO));
    }

    @Transactional
    public void update(int id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(ClientNotFoundException::new);
        LegalForm legalForm = legalFormService.findOne(clientDTO.getLegalFormName());
        client.setLegalForm(legalForm);
        client.setName(clientDTO.getName());
        client.setShortName(clientDTO.getShortName());
        client.setAddress(clientDTO.getAddress());
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id) {
        clientRepository.deleteById(id);
    }


    private Client convertToClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setShortName(clientDTO.getShortName());
        client.setAddress(clientDTO.getAddress());
        client.setLegalForm(legalFormService.findOne(clientDTO.getLegalFormName()));
        return client;
    }

    private ClientDTO convertToClientDTO(Client client) {
        ClientDTO clientDTO = modelMapper.map(client, ClientDTO.class);
        clientDTO.setLegalFormName(client.getLegalForm().getLegalForm());
        return clientDTO;
    }



}
