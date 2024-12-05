package ru.avoid.testassignment.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.avoid.testassignment.DTO.BankDTO;
import ru.avoid.testassignment.services.BanksService;
import ru.avoid.testassignment.util.BankNotCreatedException;
import ru.avoid.testassignment.util.BankNotFoundException;
import ru.avoid.testassignment.util.ClientNotCreatedException;

import java.util.List;

@RestController
@RequestMapping("/banks")
public class BanksController {

    private final BanksService banksService;
    private final ModelMapper modelMapper;


    @Autowired
    public BanksController(BanksService banksService, ModelMapper modelMapper) {
        this.banksService = banksService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<BankDTO> getBanks(@RequestParam(value = "sort_by_id", required = false, defaultValue = "false") boolean sortById,
                                  @RequestParam(value = "sort_by_bank_name", required = false, defaultValue = "false") boolean sortByBankName,
                                  @RequestParam(value = "sort_by_bank_code", required = false, defaultValue = "false") boolean sortByBankCode,
                                  @RequestParam(value = "bank_name", required = false, defaultValue = "") String bankName,
                                  @RequestParam(value = "bank_code", required = false, defaultValue = "") String bankCode) {

        // SORT BY
        return banksService.findAll(sortById, sortByBankName, sortByBankCode, bankName, bankCode);

    }
    @GetMapping("/{id}")
    public BankDTO getBankById(@PathVariable("id") int id) {
        return banksService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createBank(@RequestBody @Valid BankDTO bankDTO,
                                                   BindingResult bindingResult) {
        GeneralExceptionHandler.handleValidationErrors(bindingResult, BankNotCreatedException.class);


        banksService.save(bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBank(@PathVariable int id, @RequestBody @Valid BankDTO bankDTO, BindingResult bindingResult) {
        GeneralExceptionHandler.handleValidationErrors(bindingResult, BankNotCreatedException.class);
        banksService.update(id, bankDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBank(@PathVariable int id) {
        try {
            banksService.delete(id);
            return ResponseEntity.ok().build();
        } catch (BankNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
