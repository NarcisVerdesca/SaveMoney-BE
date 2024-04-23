package com.myprojects.savemoney.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "amount", nullable = false)
    private double amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_transaction")
    private LocalDate dateTransaction;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

}
