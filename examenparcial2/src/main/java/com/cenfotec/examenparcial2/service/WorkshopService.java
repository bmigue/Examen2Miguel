package com.cenfotec.examenparcial2.service;

import com.cenfotec.examenparcial2.domain.Workshop;

import java.util.List;
import java.util.Optional;

public interface WorkshopService {

    public void save(Workshop workshop);
    public Optional<Workshop> get(long id);
    public List<Workshop> getAll();
    public List<Workshop> find(String name);
    public List<Workshop> findCat(String name);
}
