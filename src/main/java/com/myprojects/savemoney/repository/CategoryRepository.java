package com.myprojects.savemoney.repository;

import com.myprojects.savemoney.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    
    boolean existsByNameCategory(String s);

    Category findByNameCategory(String s);
}
