package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.user.entity.User;

import java.util.List;

/**
 * user reporting contract.
 * @author kamar baraka.*/

public interface UserReportingService {

    List<User> getEmployeesNotInDept();
}
