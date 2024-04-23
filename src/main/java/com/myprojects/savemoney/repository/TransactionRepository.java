package com.myprojects.savemoney.repository;

import com.myprojects.savemoney.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
