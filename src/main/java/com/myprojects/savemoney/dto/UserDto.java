package com.myprojects.savemoney.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myprojects.savemoney.entity.Transaction;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private LocalDate birthDate;

    private String backgroundImage;

}
