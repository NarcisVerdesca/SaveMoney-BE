package com.myprojects.savemoney.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalMoneySpentDto {

    private String emailUser;

    private double moneySpent;
}
