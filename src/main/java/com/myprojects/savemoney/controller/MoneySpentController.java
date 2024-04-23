package com.myprojects.savemoney.controller;

import com.myprojects.savemoney.dto.TotalMoneySpentDto;
import com.myprojects.savemoney.dto.TransactionDto;
import com.myprojects.savemoney.exception.AmountException;
import com.myprojects.savemoney.exception.ResourceNotFoundException;
import com.myprojects.savemoney.mapper.TransactionMapper;
import com.myprojects.savemoney.service.moneyspent.IMoneySpentService;
import com.myprojects.savemoney.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class MoneySpentController {

    private static final Logger LOG = LogManager.getLogger(AuthController.class);

    @Autowired
    private IMoneySpentService iMoneySpentService;

    @Autowired
    private TransactionMapper transactionMapper;


    @GetMapping("${money-spent.get.total-money-spent}")
    public ResponseEntity<ApiResponse<TotalMoneySpentDto>> getTotalMoneySpent(){
        TotalMoneySpentDto totalMoneySpent =  iMoneySpentService.getTotalMoneySpent();
        return ResponseEntity.ok().body(ApiResponse.<TotalMoneySpentDto>builder()
                .data(totalMoneySpent)
                .build());
    }

    @GetMapping("${money-spent.transaction-list.total-money-spent}")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionList(){
        List<TransactionDto> transactionDtoList = transactionMapper.entityListToDto(iMoneySpentService.getAllTransactionOnTotalMoneySpent());
        return ResponseEntity.ok().body(ApiResponse.<List<TransactionDto>>builder()
                .data(transactionDtoList)
                .build());
    }

    @Transactional
    @PutMapping("${money-spent.write-off-money.total-money-spent}")
    public ResponseEntity<ApiResponse<String>> writeOffMoneyFromTotalMoneySpent(
            @RequestParam double amount,
            @RequestParam String title,
            @RequestParam Long idCategory) {

        try{
            String result = iMoneySpentService.writeOffMoneyFromTotalMoneySpent(amount, title, idCategory);
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


    @GetMapping("${money-spent.transaction-list.year-month.total-money-spent}")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionListOfSpecificMonth(@RequestParam int year, int month){
        List<TransactionDto> transactionDtoListOfAMonth = transactionMapper.entityListToDto(iMoneySpentService.getAllTransactionOnTotalMoneySpentOfOneMonth(year, month));
        return ResponseEntity.ok().body(ApiResponse.<List<TransactionDto>>builder()
                .data(transactionDtoListOfAMonth)
                .build());
    }

    @Transactional
    @PutMapping("${money-spent.reset.total-money-spent}")
    public ResponseEntity<ApiResponse<String>> resetTotalMoneySpent() {

        try{
            String result = iMoneySpentService.resetTotalMoneySpent();
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
