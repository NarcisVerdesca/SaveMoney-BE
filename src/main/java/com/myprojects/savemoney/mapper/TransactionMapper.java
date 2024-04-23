package com.myprojects.savemoney.mapper;

import com.myprojects.savemoney.dto.TransactionDto;
import com.myprojects.savemoney.dto.UserDto;
import com.myprojects.savemoney.entity.Transaction;
import com.myprojects.savemoney.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {

    @Autowired
    private ModelMapper modelMapper;


    public Transaction dtoToEntity(Transaction transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        return transaction;
    }

    public TransactionDto entityToDto(Transaction transaction) {
        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
        return transactionDto;
    }

    public List<TransactionDto> entityListToDto(List<Transaction> transactionList) {
        List<TransactionDto> listEntities = transactionList
                .stream()
                .map(this::entityToDto)
                .toList();
        return listEntities;
    }





}
