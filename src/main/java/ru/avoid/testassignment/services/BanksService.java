package ru.avoid.testassignment.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.avoid.testassignment.DTO.BankDTO;
import ru.avoid.testassignment.models.Bank;
import ru.avoid.testassignment.models.Client;
import ru.avoid.testassignment.models.Deposit;
import ru.avoid.testassignment.repositories.BankRepository;
import ru.avoid.testassignment.repositories.ClientRepository;
import ru.avoid.testassignment.util.BankNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BanksService {
    private final BankRepository bankRepository;

    private final ModelMapper modelMapper;



    @Autowired
    public BanksService(BankRepository bankRepository, ModelMapper modelMapper) {
        this.bankRepository = bankRepository;
        this.modelMapper = modelMapper;

    }

    public List<BankDTO> findAll(boolean sortById, boolean sortByBankName, boolean sortByBankCode, String bankName, String bankCode) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if (sortById) {
            sort = Sort.by(Sort.Direction.DESC, "id");
        }
        else if (sortByBankName) {
            sort = Sort.by(Sort.Direction.ASC, "bankName");
        }

        else if (sortByBankCode) {
            sort = Sort.by(Sort.Direction.ASC, "bankCode");
        }

        if (bankName != null && !bankName.isEmpty()) {

            return bankRepository.findAll(BankSpecifications.equalBankName(bankName), sort).stream().
                    map(this::convertToBankDTO).collect(Collectors.toList());
        }
        if (bankCode != null && !bankCode.isEmpty()) {
            return bankRepository.findAll(BankSpecifications.equalBankCode(bankCode), sort).stream().
                    map(this::convertToBankDTO).collect(Collectors.toList());
        }


        System.out.println(bankRepository.findAll(sort).stream().
                map(this::convertToBankDTO).collect(Collectors.toList()));
        return bankRepository.findAll(sort).stream().
                map(this::convertToBankDTO).collect(Collectors.toList());
    }


    public BankDTO findOne(int id) {
        Optional<Bank> foundBank = bankRepository.findById(id);
        return  convertToBankDTO(foundBank.orElseThrow(BankNotFoundException::new));
    }

    public Bank findOne(String bankName) {
        Optional<Bank> foundBank = bankRepository.findByBankName(bankName);
        return  foundBank.orElseThrow(BankNotFoundException::new);
    }

    @Transactional
    public void save(BankDTO bankDTO) {
        bankRepository.save(convertToBank(bankDTO));
    }

    @Transactional
    public void update(int id, BankDTO BankDTO) {
        Bank foundBank = bankRepository.findById(id)
                .orElseThrow(BankNotFoundException::new);
        foundBank.setBankName(BankDTO.getBankName());
        foundBank.setBankCode(BankDTO.getBankCode());
        bankRepository.save(foundBank);
    }

    @Transactional
    public void delete(int id) {
        bankRepository.deleteById(id);
    }


    public Bank convertToBank(BankDTO bankDTO) {
        return modelMapper.map(bankDTO, Bank.class);
    }

    //TODO
    public BankDTO convertToBankDTO(Bank bank) {
        return modelMapper.map(bank, BankDTO.class);
    }
}
