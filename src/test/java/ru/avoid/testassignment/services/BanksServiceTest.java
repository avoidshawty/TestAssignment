package ru.avoid.testassignment.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.avoid.testassignment.DTO.BankDTO;
import ru.avoid.testassignment.models.Bank;
import ru.avoid.testassignment.repositories.BankRepository;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.Random.class)
public class BanksServiceTest {

    @MockBean
    private BankRepository bankRepository;

    @Autowired
    private BanksService banksService;

    private List<Bank> banks;

    private Sort sort;


    @BeforeEach
    public void setUp() {
        banks = new ArrayList<>();

        Bank b1 = new Bank();
        b1.setId(1);
        b1.setBankName("bank1");
        b1.setBankCode("123456789");
        banks.add(b1);
        Bank b2 = new Bank();
        b2.setId(2);
        b2.setBankName("bank2");
        b2.setBankCode("987654321");
        banks.add(b2);
        Bank b3 = new Bank();
        b3.setId(3);
        b3.setBankName("bank3");
        b3.setBankCode("987654321");
        banks.add(b3);
        Bank b4 = new Bank();
        b4.setId(4);
        b4.setBankName("bank4");
        b4.setBankCode("123456789");
        banks.add(b4);

        sort = Sort.by(Sort.Direction.ASC, "id");

    }

    @Test
    public void findAllBanksTest() {
        when(bankRepository.findAll(sort)).thenReturn(banks);
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, null, null);
        assertEquals(banks.size(), receivedBanks.size());
        assertIterableEquals(banks.stream().map(banksService::convertToBankDTO).collect(Collectors.toList()), receivedBanks);
        verify(bankRepository, times(1)).findAll(sort);
    }

    @Test
    public void findAllBanksIfEmptyTest() {
        when(bankRepository.findAll(sort)).thenReturn(Collections.emptyList());
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, null, null);
        assertTrue(receivedBanks.isEmpty());

        verify(bankRepository, times(1)).findAll(sort);
    }

    @Test
    public void findAllBanksWithExistingNameFilterTest() {

        when(bankRepository.findAll(BankSpecifications.equalBankName("bank1"), sort)).thenReturn(banks);
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, "bank1", null);
        assertEquals(1, receivedBanks.size());
        assertEquals(banksService.convertToBankDTO(banks.get(0)), receivedBanks.get(0));
        verify(bankRepository, times(1)).findAll(BankSpecifications.equalBankName("bank1"), sort);
    }

    @Test
    public void findAllBanksWithNotExistingNameFilterTest() {
        when(bankRepository.findAll(sort)).thenReturn(banks);
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, "bank1", null);
        assertEquals(0, receivedBanks.size());
        verify(bankRepository, times(1)).findAll(sort);
    }

    @Test
    public void findAllBanksWithExistingCodeFilterTest() {
        when(bankRepository.findAll(sort)).thenReturn(banks);
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, null, null);
        assertEquals(2, receivedBanks.size());
        assertEquals(banksService.convertToBankDTO(banks.get(1)), receivedBanks.get(0));
        assertEquals(banksService.convertToBankDTO(banks.get(2)), receivedBanks.get(1));
        verify(bankRepository, times(1)).findAll(sort);
    }

    @Test
    public void findAllBanksWithNotExistingCodeFilterTest() {
        when(bankRepository.findAll(sort)).thenReturn(banks);
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, null, null);
        assertEquals(0, receivedBanks.size());
        verify(bankRepository, times(1)).findAll(sort);
    }

    @Test
    public void findAllBanksWithExistingNameAndBicFilterTest() {
        when(bankRepository.findAll(sort)).thenReturn(banks);
        List<BankDTO> receivedBanks = banksService.findAll(false, false, false, null, null);
        assertEquals(1, receivedBanks.size());
        assertEquals(banksService.convertToBankDTO(banks.get(0)), receivedBanks.get(0));
        verify(bankRepository, times(1)).findAll(sort);
    }
}
