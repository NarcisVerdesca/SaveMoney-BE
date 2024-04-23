package com.myprojects.savemoney.service.conserve;

import com.myprojects.savemoney.dto.PiggyBankDto;
import com.myprojects.savemoney.entity.Transaction;
import com.myprojects.savemoney.exception.AmountException;

import java.util.List;

public interface IConserveService {

    PiggyBankDto getPiggyBank();

    String addMoneyToPiggyBank(double amount) throws AmountException;

    String writeOffMoneyToPiggyBank(double amount) throws AmountException;

    List<Transaction> getAllTransactionOnPiggy();

    List<Transaction> getAllTransactionOnPiggyOfOneMonth(int year, int month);

    String resetPiggyBank();
}
