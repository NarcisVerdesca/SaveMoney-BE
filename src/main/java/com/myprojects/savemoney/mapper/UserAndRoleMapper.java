package com.myprojects.savemoney.mapper;

import com.myprojects.savemoney.dto.RoleDto;
import com.myprojects.savemoney.dto.UserDto;
import com.myprojects.savemoney.entity.Role;
import com.myprojects.savemoney.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAndRoleMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User dtoToUser(UserDto userDTO) {
        User userToSave = modelMapper.map(userDTO, User.class);
        userToSave.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userToSave;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        userDTO.setRoles(user.getRoles().stream().map(this::roleToRoleDto).toList());
        return userDTO;
    }

    public RoleDto roleToRoleDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }
    public Role dtoToRole(RoleDto roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

}
