package com.kamar.issuemanagementsystem.department.data;


import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * the department dto.
 * @author kamar baraka.*/

public record DepartmentDto(

        String departmentName,
        String email,
        String headOfDepartment,
        int rating,
        List<String > members
) implements DepartmentDtoType{
}
