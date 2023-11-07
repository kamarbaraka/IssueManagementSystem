package com.kamar.issuemanagementsystem.department.data;

import jakarta.validation.constraints.Email;

import java.util.List;

/**
 * dto to add a user or users to a department.
 * @author kamar baraka.*/

public record AddUserToDepartmentDTO(

        List<@Email String >  username,
        String department
) implements DepartmentDtoType {
}
