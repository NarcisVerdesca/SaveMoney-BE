package com.myprojects.savemoney.service.conserve;

import com.myprojects.savemoney.dto.PiggyBankDto;
import com.myprojects.savemoney.entity.Transaction;
import com.myprojects.savemoney.entity.User;
import com.myprojects.savemoney.exception.ResourceNotFoundException;
import com.myprojects.savemoney.repository.CategoryRepository;
import com.myprojects.savemoney.repository.TransactionRepository;
import com.myprojects.savemoney.repository.UserRepository;
import com.myprojects.savemoney.exception.AmountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConserveService implements IConserveService{
    private static final Logger LOG = LoggerFactory.getLogger(ConserveService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public PiggyBankDto getPiggyBank() {
        String loggedUser = getLoggedUser();
        User currentUser = userRepository.findByEmail(loggedUser);
        PiggyBankDto piggyBankDto = new PiggyBankDto();
        piggyBankDto.setEmailUser(currentUser.getEmail());
        piggyBankDto.setMoneySaved(currentUser.getPiggyBank());
        LOG.info("User Piggy Bank:  " + currentUser.getPiggyBank());
        return piggyBankDto;
    }

    @Override
    public String addMoneyToPiggyBank(double amount) throws AmountException {

        String loggedUser = getLoggedUser();

        validateAmount(amount);

        addToPiggyBank(loggedUser, amount);

        recordTransaction(loggedUser, amount);

        return "Money added successfully!";
    }

    @Override
    public String writeOffMoneyToPiggyBank(double amount) throws AmountException {
        String loggedUser = getLoggedUser();

        deleteToPiggyBank(loggedUser, amount);

        recordTransactionWriteOff(loggedUser, amount);

        return amount + " write off successfully!";
    }

    @Override
    public List<Transaction> getAllTransactionOnPiggy() {
        String loggedUser = getLoggedUser();
        List<Transaction> listOfTransactionOfUserForPiggy = transactionRepository.findAll()
                .stream()
                .filter
                        (t ->
                                        t.getUser().getEmail().equals(loggedUser)
                                                &&
                                                isCategoryOfSaveMoney(t)
                        )
                .toList();
        return listOfTransactionOfUserForPiggy;
    }


    @Override
    public List<Transaction> getAllTransactionOnPiggyOfOneMonth(int year, int month) {

        String loggedUser = getLoggedUser();

        return transactionRepository.findAll()
                .stream()
                .filter(
                        t ->
                                t.getUser().getEmail().equals(loggedUser)
                                &&
                                isTransactionInMonthAndYear(t, year, month)
                                &&
                                isCategoryOfSaveMoney(t)

                )
                .toList();
    }

    @Override
    public String resetPiggyBank() {
        String loggedUser = getLoggedUser();
       User user =  userRepository.findByEmail(loggedUser);
       user.setPiggyBank(0.0);
       userRepository.save(user);

        return "Piggy Bank of " + loggedUser + " reset successfully!";
    }

    private boolean isTransactionInMonthAndYear(Transaction transaction, int year, int month) {

        LocalDate transactionDate = transaction.getDateTransaction();

        return transactionDate.getYear() == year && transactionDate.getMonthValue() == month;

    }

    private void validateAmount(double amount) throws AmountException {
        if(String.valueOf(amount).length() > 10){
            throw new AmountException("Amount cannot exceed 10 digits");
        }
    }

    private String getLoggedUser() {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }
        return loggedUser;
    }
    private void addToPiggyBank(String userEmail, double amount) {
        User currentUser = userRepository.findByEmail(userEmail);
        double piggyBankOfCurrentUser = currentUser.getPiggyBank();
        piggyBankOfCurrentUser += amount;
        currentUser.setPiggyBank(piggyBankOfCurrentUser);
        userRepository.save(currentUser);
        LOG.info("User added {} to his piggy bank: ", amount);
    }

    private void deleteToPiggyBank(String userEmail, double amount) throws AmountException {
        User currentUser = userRepository.findByEmail(userEmail);
        double piggyBankOfCurrentUser = currentUser.getPiggyBank();
        validateAmountAgainstPiggyBank(amount, piggyBankOfCurrentUser);
        piggyBankOfCurrentUser -= amount;
        currentUser.setPiggyBank(piggyBankOfCurrentUser);
        userRepository.save(currentUser);
    }

    private void validateAmountAgainstPiggyBank(double amount, double piggyBankOfCurrentUser) throws AmountException {
        if(amount > piggyBankOfCurrentUser){
            throw new AmountException("Amount is bigger than your Piggy Bank balance");
        }
    }

    private void recordTransactionWriteOff(String userEmail, double amount) {
        User currentUser = userRepository.findByEmail(userEmail);
        Transaction transaction = new Transaction();
        transaction.setTitle(null);
        transaction.setAmount(amount);
        transaction.setDateTransaction(LocalDate.now());
        transaction.setUser(currentUser);
        transaction.setCategory(categoryRepository.findByNameCategory("Write Off Money from Piggy Bank!"));
        transactionRepository.save(transaction);
    }
    private void recordTransaction(String userEmail, double amount) {
        User currentUser = userRepository.findByEmail(userEmail);
        Transaction transaction = new Transaction();
        transaction.setTitle(null);
        transaction.setAmount(amount);
        transaction.setDateTransaction(LocalDate.now());
        transaction.setUser(currentUser);
        transaction.setCategory(categoryRepository.findByNameCategory("Save Money!"));
        transactionRepository.save(transaction);
    }

    private boolean isCategoryOfSaveMoney(Transaction t) {
        return t.getCategory().getNameCategory().equals("Save Money!")
                ||
                (t.getCategory().getNameCategory().equals("Write Off Money from Piggy Bank!"));
    }



}
