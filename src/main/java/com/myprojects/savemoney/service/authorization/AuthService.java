package com.myprojects.savemoney.service.authorization;

import com.myprojects.savemoney.dto.UserDto;
import com.myprojects.savemoney.entity.Role;
import com.myprojects.savemoney.entity.User;
import com.myprojects.savemoney.exception.ResourceNotFoundException;
import com.myprojects.savemoney.exception.UserDataException;
import com.myprojects.savemoney.jwt.JwtTokenProvider;
import com.myprojects.savemoney.jwt.LoginDto;
import com.myprojects.savemoney.mapper.UserAndRoleMapper;
import com.myprojects.savemoney.repository.RoleRepository;
import com.myprojects.savemoney.repository.UserRepository;
import com.myprojects.savemoney.utility.EmailValidator;
import com.myprojects.savemoney.utility.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AuthService implements IAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider jwtTokenProvider;

    private UserAndRoleMapper userAndRoleMapper;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserAndRoleMapper userAndRoleMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAndRoleMapper = userAndRoleMapper;
    }

    @Override
    public String registerUser(UserDto userDto) throws UserDataException {
        return registerUserOrAdmin(userDto, "ROLE_USER");
    }

    @Override
    public String registerAdmin(UserDto userDto) throws UserDataException {
        return registerUserOrAdmin(userDto, "ROLE_ADMIN");
    }

    private String registerUserOrAdmin(UserDto userDto, String roleType) throws UserDataException {
        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (userRepository.existsByEmail(emailDto) || emailDto.isEmpty() || !EmailValidator.isValidEmail(emailDto)) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }
        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(emailDto);
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToSave.setBirthDate(userDto.getBirthDate());
        userToSave.setBackgroundImage(userDto.getBackgroundImage());

        Role role = roleToAssign(roleType);
        userToSave.setRoles(List.of(role));

        User userSaved = userRepository.save(userToSave);
        if (!userRepository.existsByEmail(userSaved.getEmail())) {
            throw new UserDataException(UserDataException.somethingGoesWrong());
        }
        if ("ROLE_USER".equals(roleType)) {
            LOG.info("User registered: " + userSaved.getEmail());
            return "User registered successfully";
        } else {
            return "Admin registered successfully";
        }
    }

    @Override
    public User getUserByEmail() {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }
        LOG.info("User info:  " + loggedUser);
        return userRepository.findByEmail(loggedUser);
    }

    @Override
    public String updateUser(UserDto updateUserDto) throws UserDataException {

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }

        User existingUser = userRepository.findByEmail(loggedUser);
        existingUser.setFirstName(updateUserDto.getFirstName());
        existingUser.setLastName(updateUserDto.getLastName());

        User updatedUser = userRepository.save(existingUser);

        // Controlla se l'utente è stato effettivamente aggiornato nel database
        if (updatedUser == null) {
            throw new UserDataException("Failed to update user!");
        }

        LOG.info("User updated: " + updatedUser.getEmail());
        return "User updated successfully";
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userRepository.findAll();
    }

    public String login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.generateToken(authentication);
        } catch (org.springframework.security.core.AuthenticationException e) {
            if (e instanceof org.springframework.security.authentication.BadCredentialsException) {
                throw new BadCredentialsException("Credenziali non valide", e);
            }
            throw new AccessDeniedException("Accesso vietato", e);
        }
    }

    public Role roleToAssign(String nameRole) {
        Role role = roleRepository.findByName(nameRole);
        if (role == null) {
            Role newRole = new Role();
            newRole.setName(nameRole);
            role = roleRepository.save(newRole);
        }
        return role;
    }

}
