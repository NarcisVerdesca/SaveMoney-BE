package com.myprojects.savemoney.repository;

import com.myprojects.savemoney.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String nameRole);


}
