package com.myprojects.savemoney.service.authorization;

import com.myprojects.savemoney.dto.UserDto;
import com.myprojects.savemoney.entity.User;
import com.myprojects.savemoney.exception.UserDataException;
import com.myprojects.savemoney.jwt.LoginDto;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;

public interface IAuthService {

    String registerUser(UserDto userDto) throws UserDataException;

    String login(LoginDto loginDto) ;

    String registerAdmin(UserDto userDto, String keyForAdmin) throws UserDataException;

    User getUserByEmail();

    String updateUser(UserDto userDto) throws UserDataException;

    List<User> getAllUsers();
}
