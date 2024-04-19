package com.myprojects.savemoney.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private long id;

    private String name;

    @JsonIgnoreProperties("roles")
    private List<UserDto> users;

    private boolean deleted;

}
