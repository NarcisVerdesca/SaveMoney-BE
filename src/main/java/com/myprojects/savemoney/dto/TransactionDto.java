package com.myprojects.savemoney.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myprojects.savemoney.entity.Category;
import com.myprojects.savemoney.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String title;

    private double amount;

    private LocalDate dateTransaction;

    private String userEmail; //user

    private String category;    //category

}
