package com.myprojects.savemoney.service.moneyspent;

import com.myprojects.savemoney.dto.TotalMoneySpentDto;
import com.myprojects.savemoney.entity.Category;
import com.myprojects.savemoney.entity.Transaction;
import com.myprojects.savemoney.exception.AmountException;

import java.util.List;

public interface IMoneySpentService {

    TotalMoneySpentDto getTotalMoneySpent();

    List<Transaction> getAllTransactionOnTotalMoneySpent();

    String writeOffMoneyFromTotalMoneySpent(double amount, String title, Long idCategory) throws AmountException;

    List<Transaction> getAllTransactionOnTotalMoneySpentOfOneMonth(int year, int month);

    String resetTotalMoneySpent();
}
