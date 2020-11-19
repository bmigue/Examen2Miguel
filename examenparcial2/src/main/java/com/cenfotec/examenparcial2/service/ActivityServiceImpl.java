package com.cenfotec.examenparcial2.service;

import com.cenfotec.examenparcial2.domain.Activity;
import com.cenfotec.examenparcial2.repo.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    ActivityRepository activityRepo;



    @Override
    public void save(Activity act) {
        activityRepo.save(act);
    }
}
