package com.myprojects.savemoney.controller;

import com.myprojects.savemoney.dto.PiggyBankDto;
import com.myprojects.savemoney.dto.TransactionDto;
import com.myprojects.savemoney.exception.AmountException;
import com.myprojects.savemoney.exception.ResourceNotFoundException;
import com.myprojects.savemoney.mapper.TransactionMapper;
import com.myprojects.savemoney.service.conserve.IConserveService;
import com.myprojects.savemoney.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class ConserveController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IConserveService iConserveService;

    @Autowired
    private TransactionMapper transactionMapper;

    @GetMapping("${conserve.get.piggyBank}")
    public ResponseEntity<ApiResponse<PiggyBankDto>> getPiggyBankDto(){
        PiggyBankDto piggyBankDto =  iConserveService.getPiggyBank();
        return ResponseEntity.ok().body(ApiResponse.<PiggyBankDto>builder()
                .data(piggyBankDto)
                .build());
    }

    @GetMapping("${conserve.transaction-list.piggyBank}")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionList(){
        List<TransactionDto> transactionDtoList = transactionMapper.entityListToDto(iConserveService.getAllTransactionOnPiggy());
        return ResponseEntity.ok().body(ApiResponse.<List<TransactionDto>>builder()
                .data(transactionDtoList)
                .build());
    }

    @GetMapping("${conserve.transaction-list.year-month.piggyBank}")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionListOfSpecificMonth(@RequestParam int year, int month){
        List<TransactionDto> transactionDtoListOfAMonth = transactionMapper.entityListToDto(iConserveService.getAllTransactionOnPiggyOfOneMonth(year, month));
        return ResponseEntity.ok().body(ApiResponse.<List<TransactionDto>>builder()
                .data(transactionDtoListOfAMonth)
                .build());
    }

    @Transactional
    @PutMapping("${conserve.add-money.piggyBank}")
    public ResponseEntity<ApiResponse<String>> addMoneyToPiggyBank(@RequestParam double amount) {

        try{
            String result = iConserveService.addMoneyToPiggyBank(amount);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data(result)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch(AmountException amountException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                    .message(amountException.getMessage())
                    .build());
        }

    }

    @Transactional
    @PutMapping("${conserve.delete-money.piggyBank}")
    public ResponseEntity<ApiResponse<String>> writeOffMoneyToPiggyBank(@RequestParam double amount) {

        try{
            String result = iConserveService.writeOffMoneyToPiggyBank(amount);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data(result)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (AmountException amountException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                    .message(amountException.getMessage())
                    .build());
        }
    }

    @Transactional
    @PutMapping("${conserve.reset.piggyBank}")
    public ResponseEntity<ApiResponse<String>> resetPiggyBank() {

        try{
            String result = iConserveService.resetPiggyBank();
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data(result)
                    .build());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(e.getMessage())
                    .build());
        }
    }











}
