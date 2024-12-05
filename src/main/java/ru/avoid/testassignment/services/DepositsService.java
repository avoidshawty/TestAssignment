package ru.avoid.testassignment.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avoid.testassignment.DTO.DepositDTO;
import ru.avoid.testassignment.models.Deposit;
import ru.avoid.testassignment.repositories.DepositRepository;
import ru.avoid.testassignment.util.DepositNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepositsService {
    private final DepositRepository depositRepository;
    private final ModelMapper modelMapper;
    private final BanksService banksService;
    private final ClientsService clientsService;

    @Autowired
    public DepositsService(DepositRepository depositRepository, ModelMapper modelMapper, BanksService banksService, ClientsService clientsService) {
        this.depositRepository = depositRepository;
        this.modelMapper = modelMapper;
        this.banksService = banksService;
        this.clientsService = clientsService;
    }

    public List<DepositDTO> findAll(boolean sortById, boolean sortByOpeningDate, boolean sortByInterest,
                                    boolean sortByDurationInMonths, double interest,
                                    int durationInMonths, String clientName, String bankName) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (sortById) {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        else if (sortByOpeningDate) {
            sort = Sort.by(Sort.Direction.ASC, "openingDate");
        }

        else if (sortByInterest) {
            sort = Sort.by(Sort.Direction.ASC, "interest");
        }

        else if (sortByDurationInMonths) {
            sort = Sort.by(Sort.Direction.ASC, "durationInMonths");
        }

        if (interest != 0) {
            return depositRepository.findAll(DepositsSpecifications.equalInterest(interest), sort).stream().
                    map(this::convertToDepositDTO).collect(Collectors.toList());
        }

        if (durationInMonths != 0) {
            return depositRepository.findAll(DepositsSpecifications.equalDurationInMonths(durationInMonths), sort).stream().
                    map(this::convertToDepositDTO).collect(Collectors.toList());
        }

        if (clientName != null && !clientName.isEmpty()) {

            return depositRepository.findAll(DepositsSpecifications.equalClientName(clientName), sort).stream().
                    map(this::convertToDepositDTO).collect(Collectors.toList());
        }

        if (bankName != null && !bankName.isEmpty()) {

            return depositRepository.findAll(DepositsSpecifications.equalBankName(bankName), sort).stream().
                    map(this::convertToDepositDTO).collect(Collectors.toList());
        }

        return depositRepository.findAll(sort).stream().
                map(this::convertToDepositDTO).collect(Collectors.toList());
    }


    public DepositDTO findOne(int id) {
        Optional<Deposit> foundDeposit = depositRepository.findById(id);
        return convertToDepositDTO(foundDeposit.orElseThrow(DepositNotFoundException::new)) ;
    }

    @Transactional
    public void save(DepositDTO depositDTO) {
        depositRepository.save(convertToDeposit(depositDTO));
    }

    @Transactional
    public void update(int id, DepositDTO depositDTO) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(DepositNotFoundException::new);
        deposit.setBankDeposit(banksService.findOne(depositDTO.getBankDeposit()));
        deposit.setDepositor(clientsService.findOne(depositDTO.getDepositor()));
        deposit.setInterest(depositDTO.getInterest());
        deposit.setOpeningDate(depositDTO.getOpeningDate());
        deposit.setDurationInMonths(depositDTO.getDurationInMonths());
        depositRepository.save(deposit);
    }

    @Transactional
    public void delete(int id) {
        depositRepository.deleteById(id);
    }

    public Optional<Deposit> findById(int id) {
        return depositRepository.findById(id);
    }

    private Deposit convertToDeposit(DepositDTO depositDTO) {
        Deposit deposit = new Deposit();
        deposit.setDurationInMonths(depositDTO.getDurationInMonths());
        deposit.setOpeningDate(depositDTO.getOpeningDate());
        deposit.setInterest(depositDTO.getInterest());
        deposit.setBankDeposit(banksService.findOne(depositDTO.getBankDeposit()));
        deposit.setDepositor(clientsService.findOne(depositDTO.getDepositor()));
        return deposit;
    }

    private DepositDTO convertToDepositDTO(Deposit deposit) {
        DepositDTO depositDTO = new DepositDTO();
        depositDTO.setDepositor(deposit.getDepositor().getName());
        depositDTO.setInterest(deposit.getInterest());
        depositDTO.setDurationInMonths(deposit.getDurationInMonths());
        depositDTO.setOpeningDate(deposit.getOpeningDate());
        depositDTO.setBankDeposit(deposit.getBankDeposit().getBankName());
        return depositDTO;
    }
}
