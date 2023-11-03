package com.kamar.issuemanagementsystem.analysis.service;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;

/**
 * the department analysis contract.
 * @author kamar baraka.*/

public interface DepartmentAnalysisService {

    DepartmentDto getBestPerformantDepartment() throws AnalysisException;
    DepartmentDto getMostPerformantDepartment() throws AnalysisException;
    UserPresentationDTO getMostPerformantEmployeeInDepartment(String departmentName) throws AnalysisException;
    UserPresentationDTO getBestEmployeeInDepartment(String departmentName) throws AnalysisException;
}
