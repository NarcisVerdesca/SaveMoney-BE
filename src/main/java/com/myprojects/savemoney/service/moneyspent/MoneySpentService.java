package com.myprojects.savemoney.service.moneyspent;

import com.myprojects.savemoney.dto.PiggyBankDto;
import com.myprojects.savemoney.dto.TotalMoneySpentDto;
import com.myprojects.savemoney.entity.Category;
import com.myprojects.savemoney.entity.Transaction;
import com.myprojects.savemoney.entity.User;
import com.myprojects.savemoney.exception.AmountException;
import com.myprojects.savemoney.exception.ResourceNotFoundException;
import com.myprojects.savemoney.repository.CategoryRepository;
import com.myprojects.savemoney.repository.TransactionRepository;
import com.myprojects.savemoney.repository.UserRepository;
import com.myprojects.savemoney.service.conserve.ConserveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MoneySpentService implements IMoneySpentService{

    private static final Logger LOG = LoggerFactory.getLogger(ConserveService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TotalMoneySpentDto getTotalMoneySpent() {
        String loggedUser = getLoggedUser();
        User currentUser = userRepository.findByEmail(loggedUser);
        TotalMoneySpentDto totalMoneySpentDto = new TotalMoneySpentDto();
        totalMoneySpentDto.setEmailUser(currentUser.getEmail());
        totalMoneySpentDto.setMoneySpent(currentUser.getTotalMoneySpent());
        LOG.info("User Total Money Spent:  " + currentUser.getTotalMoneySpent());
        return totalMoneySpentDto;
    }

    @Override
    public List<Transaction> getAllTransactionOnTotalMoneySpent() {
        String loggedUser = getLoggedUser();
        return transactionRepository.findAll()
                .stream()
                .filter
                        (t ->
                                t.getUser().getEmail().equals(loggedUser)
                                        &&
                                        isCategoryOfTotalMoneySpent(t)
                        )
                .toList();
    }

    @Override
    public String writeOffMoneyFromTotalMoneySpent(double amount, String title, Long idCategory) throws AmountException {
            String loggedUser = getLoggedUser();

            deleteFromTotalMoneySpent(loggedUser, amount);

            recordTransactionWriteOff(loggedUser, amount, title, idCategory);

            return amount + " write off successfully!";

    }

    @Override
    public List<Transaction> getAllTransactionOnTotalMoneySpentOfOneMonth(int year, int month) {
        String loggedUser = getLoggedUser();
        return transactionRepository.findAll()
                .stream()
                .filter(
                        t ->
                                t.getUser().getEmail().equals(loggedUser)
                                        &&
                                        isTransactionInMonthAndYear(t, year, month)
                                        &&
                                        isCategoryOfTotalMoneySpent(t)

                )
                .toList();
    }

    @Override
    public String resetTotalMoneySpent() {
        String loggedUser = getLoggedUser();
        User user =  userRepository.findByEmail(loggedUser);
        user.setTotalMoneySpent(0.0);
        userRepository.save(user);
        return "Total money spent of " + loggedUser + " reset successfully!";
    }

    private boolean isTransactionInMonthAndYear(Transaction transaction, int year, int month) {

        LocalDate transactionDate = transaction.getDateTransaction();

        return transactionDate.getYear() == year && transactionDate.getMonthValue() == month;

    }

    private void recordTransactionWriteOff(String userEmail, double amount, String title, Long idCategory) {
        User currentUser = userRepository.findByEmail(userEmail);
        if(title == null || title.isEmpty()){
            throw new ResourceNotFoundException("Category does not exists!");
        }
        Transaction transaction = new Transaction();
        transaction.setTitle(title);
        transaction.setAmount(amount);
        transaction.setDateTransaction(LocalDate.now());
        transaction.setUser(currentUser);

       boolean isCategoryForTotalMoneySpent =  isCategoryForTotalMoneySpent(idCategory);

       if(!isCategoryForTotalMoneySpent){
           throw new ResourceNotFoundException("The Category is Incorrect!");
       }
        transaction.setCategory(categoryRepository.findById(idCategory).orElseThrow(()-> new ResourceNotFoundException("Category does not exists")));
        transactionRepository.save(transaction);
    }

    private boolean isCategoryForTotalMoneySpent(Long idCategory){
        return categoryRepository.findById(idCategory).orElseThrow(()-> new ResourceNotFoundException("Category does not exists"))
                .getNameCategory().equals("Money spent for me!")
                ||
                categoryRepository.findById(idCategory).orElseThrow(()-> new ResourceNotFoundException("Category does not exists"))
                        .getNameCategory().equals("Money spent for taxes!");
    }
    private void deleteFromTotalMoneySpent(String userEmail, double amount) throws AmountException {
        User currentUser = userRepository.findByEmail(userEmail);
        double totalMoneySpentCurrentUser = currentUser.getTotalMoneySpent();
        totalMoneySpentCurrentUser -= amount;
        currentUser.setTotalMoneySpent(totalMoneySpentCurrentUser);
        userRepository.save(currentUser);
    }

    private boolean isCategoryOfTotalMoneySpent(Transaction t) {
        return t.getCategory().getNameCategory().equals("Money spent for me!")
                ||
                (t.getCategory().getNameCategory().equals("Money spent for taxes!"));
    }

    private String getLoggedUser() {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }
        return loggedUser;
    }



}
