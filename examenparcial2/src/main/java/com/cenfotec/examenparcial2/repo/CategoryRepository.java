package com.cenfotec.examenparcial2.repo;

import com.cenfotec.examenparcial2.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findByNameContaining(String word);

}
