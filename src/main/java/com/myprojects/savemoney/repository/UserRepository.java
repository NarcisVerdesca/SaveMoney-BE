package com.myprojects.savemoney.repository;

import com.myprojects.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByIdAndDeletedFalse(long idOfUser);


}
