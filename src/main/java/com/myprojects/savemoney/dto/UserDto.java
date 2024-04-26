package com.myprojects.savemoney.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myprojects.savemoney.entity.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private double piggyBank;

    private double totalMoneySpent;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

    private String lastModifiedBy;


}
