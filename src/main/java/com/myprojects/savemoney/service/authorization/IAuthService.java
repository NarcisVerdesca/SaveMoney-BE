package com.myprojects.savemoney.service.authorization;

import com.myprojects.savemoney.dto.UserDto;
import com.myprojects.savemoney.entity.User;
import com.myprojects.savemoney.exception.UserDataException;
import com.myprojects.savemoney.jwt.LoginDto;

public interface IAuthService {

    String registerStudent(UserDto userDto) throws UserDataException;

    String login(LoginDto loginDto) ;

    String registerTutor(UserDto userDto) throws UserDataException;

    String registerAdmin(UserDto userDto) throws UserDataException;

    User getUserByEmail();

    String updateUser(UserDto userDto) throws UserDataException;

}
