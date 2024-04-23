package com.myprojects.savemoney.appconfig;

import com.myprojects.savemoney.entity.Category;
import com.myprojects.savemoney.entity.Role;
import com.myprojects.savemoney.entity.User;
import com.myprojects.savemoney.repository.CategoryRepository;
import com.myprojects.savemoney.repository.RoleRepository;
import com.myprojects.savemoney.repository.UserRepository;
import com.myprojects.savemoney.service.authorization.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        populateAdmin();
        populateCategory();
        populateUser();
    }

    private void populateAdmin() {

        if (
                userRepository.findByEmail("admin@gmail.com") == null
                        &&
                        roleRepository.findByName("ROLE_ADMIN") == null
        ) {
            User admin = new User();
            admin.setBirthDate(LocalDate.of(1996, 7, 5));
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("Password!"));
            admin.setCreateDate(LocalDateTime.now());
            Role role = authService.roleToAssign("ROLE_ADMIN");
            admin.setRoles(List.of(role));
            User adminSaved = userRepository.save(admin);
        }
    }
    private void populateUser() {

        if (
                userRepository.findByEmail("narcisverdesca1@gmail.com") == null
                        &&
                        roleRepository.findByName("ROLE_USER") == null
        ) {
            User user = new User();
            user.setBirthDate(LocalDate.of(1996, 7, 5));
            user.setFirstName("Narcis");
            user.setLastName("Verdesca");
            user.setEmail("narcisverdesca1@gmail.com");
            user.setPassword(passwordEncoder.encode("Password!"));
            user.setCreateDate(LocalDateTime.now());
            Role role = authService.roleToAssign("ROLE_USER");
            user.setRoles(List.of(role));
            User adminSaved = userRepository.save(user);
        }
    }


    //Populate Category
    private void populateCategory() {

        if (!categoryRepository.existsByNameCategory("Save Money!")) {
            Category category = new Category();
            category.setNameCategory("Save Money!");
            categoryRepository.save(category);
        }

        if(!categoryRepository.existsByNameCategory("Write Off Money from Piggy Bank!")){
            Category category = new Category();
            category.setNameCategory("Write Off Money from Piggy Bank!");
            categoryRepository.save(category);
        }

        if(!categoryRepository.existsByNameCategory("Money spent for me!")){
            Category category = new Category();
            category.setNameCategory("Money spent for me!");
            categoryRepository.save(category);
        }

        if(!categoryRepository.existsByNameCategory("Money spent for taxes!")){
            Category category = new Category();
            category.setNameCategory("Money spent for taxes!");
            categoryRepository.save(category);
        }

    }



}
