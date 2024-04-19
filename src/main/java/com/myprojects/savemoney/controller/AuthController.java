package com.myprojects.savemoney.controller;

import com.myprojects.savemoney.dto.UserDto;
import com.myprojects.savemoney.exception.ResourceNotFoundException;
import com.myprojects.savemoney.exception.UserDataException;
import com.myprojects.savemoney.jwt.JwtAuthResponse;
import com.myprojects.savemoney.jwt.LoginDto;
import com.myprojects.savemoney.mapper.UserAndRoleMapper;
import com.myprojects.savemoney.service.authorization.IAuthService;
import com.myprojects.savemoney.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private IAuthService iAuthService;

    private UserAndRoleMapper userAndRoleMapper;

    @PostMapping("${register.user.uri}") //register user
    public ResponseEntity<String> registerStudent(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerStudent(userDto);

            LOG.info("Student successfully registered: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){

            LOG.error("Error while registering Student: {}", userDataException.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }

    @PostMapping("${register.admin.uri}") //register tutor
    public ResponseEntity<String> registerAdmin(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerAdmin(userDto);
            LOG.info("Admin successfully registered: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            LOG.error("Admin successfully registered: {}", userDto.getEmail());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }

    @PostMapping("${login.uri}")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        try {
            String token = iAuthService.login(loginDto);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setAccessToken(token);
            LOG.info("User logged in successfully: {}", loginDto.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(jwtAuthResponse);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtAuthResponse("Bad credentials!"));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new JwtAuthResponse("Access denied!"));
        }
    }

    @GetMapping("${detail.userdto}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail() {
        try{
            UserDto userDTO = userAndRoleMapper.userToUserDto(iAuthService.getUserByEmail());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponse.<UserDto>builder()
                            .data(userDTO)
                            .build());
        }catch (ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<UserDto>builder()
                            .message(resourceNotFoundException.getMessage())
                            .build());
        }
    }

    @PutMapping("${update.user.uri}")
    public ResponseEntity<String> updateUser(
            @RequestBody UserDto userDto
    ) throws UserDataException {
        try {
            String response = iAuthService.updateUser(userDto);
            LOG.info("User successfully updated: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (UserDataException userDataException) {
            LOG.error("Error while updating user: {}", userDataException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }
    }


}
