package com.cenfotec.examenparcial2.repo;

import com.cenfotec.examenparcial2.domain.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long>{
    public List<Workshop> findByNameContaining(String word);
    public List<Workshop> findByCategoryContaining(String word);
    public List<Workshop> findByAuthorContaining(String word);

}

