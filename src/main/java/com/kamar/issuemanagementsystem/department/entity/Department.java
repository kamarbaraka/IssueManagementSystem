package com.kamar.issuemanagementsystem.department.entity;

import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * the department entity.
 * @author kamar baraka.*/

@Entity(name = "departments")
@Data
public class Department {

    @Id
    @Column(name = "department_name", unique = true, updatable = false, nullable = false)
    @Size(min = 2, max = 50, message = "department name too long")
    private String departmentName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "head_of_department")
    private User headOfDepartment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_name")
    private final Collection< User > members = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "rating", columnDefinition = "VARCHAR(255)")
    private Rating rating = new Rating();

}
