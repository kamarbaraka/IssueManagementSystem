package com.kamar.issuemanagementsystem.department.data;


import org.springframework.beans.factory.annotation.Value;

/**
 * the department dto.
 * @author kamar baraka.*/

public record DepartmentDto(

        String departmentName,
        String headOfDepartment,
        int rating
) implements DepartmentDtoType{
}