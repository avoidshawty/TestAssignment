package ru.avoid.testassignment.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.avoid.testassignment.DTO.DepositDTO;
import ru.avoid.testassignment.services.DepositsService;
import ru.avoid.testassignment.util.ClientNotCreatedException;
import ru.avoid.testassignment.util.DepositNotCreatedException;
import ru.avoid.testassignment.util.DepositNotFoundException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/deposits")
public class DepositsController {
    private final DepositsService depositsService;
    private final ModelMapper modelMapper;

    @Autowired
    public DepositsController(DepositsService depositsService, ModelMapper modelMapper) {
        this.depositsService = depositsService;
        this.modelMapper = modelMapper;
    }




    @GetMapping()
    public List<DepositDTO> getDeposits(@RequestParam(value = "sort_by_id", required = false, defaultValue = "false") boolean sortById,
                                        @RequestParam(value = "sort_by_opening_date", required = false, defaultValue = "false") boolean sortByOpeningDate,
                                        @RequestParam(value = "sort_by_interest", required = false, defaultValue = "false") boolean sortByInterest,
                                        @RequestParam(value = "sort_by_duration", required = false, defaultValue = "false") boolean sortByDurationInMonths,
                                        @RequestParam(value = "interest", required = false, defaultValue = "0.0") double interest,
                                        @RequestParam(value = "duration_in_months", required = false, defaultValue = "0") int durationInMonths,
                                        @RequestParam(value = "client_name", required = false, defaultValue = "") String clientName,
                                        @RequestParam(value = "bank_name", required = false, defaultValue = "") String bankName) {

        // SORT BY

        return depositsService.findAll(sortById, sortByOpeningDate, sortByInterest,
                        sortByDurationInMonths, interest, durationInMonths, clientName, bankName);

    }

    @GetMapping("/{id}")
    public DepositDTO getDepositById(@PathVariable("id") int id) {
        return depositsService.findOne(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createDeposit(@RequestBody @Valid DepositDTO depositDTO,
                                                 BindingResult bindingResult) {
        GeneralExceptionHandler.handleValidationErrors(bindingResult, DepositNotCreatedException.class);

        depositsService.save(depositDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateDeposit(@PathVariable int id, @RequestBody @Valid DepositDTO depositDTO, BindingResult bindingResult) {
        GeneralExceptionHandler.handleValidationErrors(bindingResult, DepositNotCreatedException.class);
        depositsService.update(id, depositDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDeposit(@PathVariable int id) {
        try {
            depositsService.delete(id);
            return ResponseEntity.ok().build();
        } catch (DepositNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
