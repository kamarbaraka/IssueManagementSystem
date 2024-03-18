/*
package com.kamar.issuemanagementsystem.department.entity;

import com.kamar.issuemanagementsystem.rating.entity.DepartmentPerformanceRating;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

*/
/**
 * the department entity.
 * @author kamar baraka.*//*


@Entity(name = "departments")
@Data
public class Department implements Serializable {

    @Id
    @Column(name = "department_name", unique = true, updatable = false, nullable = false)
    @Size(min = 2, max = 50, message = "department name too long")
    private String departmentName;

    @Email
    @Column(name = "department_email", nullable = false)
    private String departmentEmail;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            orphanRemoval = true)
    @JoinColumn(name = "hod")
    private UserEntity headOfDepartment;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "department")
    private final Collection<UserEntity> members = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "rating")
    private final DepartmentPerformanceRating performanceRating = new DepartmentPerformanceRating();

}
*/
