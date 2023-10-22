package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.entity.User;

/**
 * the user analysis service.
 * @author kamar baraka.*/

public interface UserAnalysisService {

    User mostPerformantEmployee();
    User leastPerformantEmployee();
}
