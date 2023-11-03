package com.kamar.issuemanagementsystem.analysis.service;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.department.utility.DepartmentMapper;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * implementation of the department analysis service contract.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class DepartmentAnalysisServiceImpl implements DepartmentAnalysisService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserMapper userMapper;
    
    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public DepartmentDto getBestPerformantDepartment() throws AnalysisException {

        /*get the best performant department*/
        List<Department> allDepartments = departmentRepository.findAll();

        return allDepartments.parallelStream().reduce(
                        (department, department2) ->
                                department2.getRating().getRate() > department.getRating().getRate() ? department2 : department)
                .map(departmentMapper::mapToDto)
                .orElseThrow(() -> new AnalysisException("sorry an error  occurred "));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public DepartmentDto getMostPerformantDepartment() throws AnalysisException {

        /*get all departments*/
        List<Department> allDepartments = departmentRepository.findAll();

        /*get the most performant*/
        return allDepartments.parallelStream().reduce((department, department2) ->
                        department2.getRating().getTotalRates() > department.getRating().getTotalRates() ? department2 : department)
                .map(departmentMapper::mapToDto).orElseThrow(() ->
                        new AnalysisException("sorry an error occurred !"));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO getMostPerformantEmployeeInDepartment(String departmentName) throws AnalysisException {

        /*get the department by name*/
        Department department = departmentRepository.findById(departmentName).orElseThrow();
        /*get the users*/
        return department.getMembers().parallelStream().reduce((member, member2) ->
                member2.getRating().getTotalRates() > member.getRating().getTotalRates() ? member2 : member).map(
                userMapper::userToPresentationDTO
        ).orElseThrow(() -> new AnalysisException("an error occurred!"));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO getBestEmployeeInDepartment(String departmentName) throws AnalysisException {

        /*get the department*/
        Department department = departmentRepository.findById(departmentName).orElseThrow();
        /*get the best user*/
        return department.getMembers().parallelStream().reduce((member, member2) ->
                        member2.getRating().getRate() > member.getRating().getRate() ? member2 : member)
                .map(userMapper::userToPresentationDTO).orElseThrow(
                        () -> new AnalysisException("error occurred!")
                );
    }
}
