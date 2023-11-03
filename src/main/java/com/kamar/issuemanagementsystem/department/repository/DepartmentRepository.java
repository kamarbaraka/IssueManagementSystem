package com.kamar.issuemanagementsystem.department.repository;

import com.kamar.issuemanagementsystem.department.data.projection.DepartmentDtoProjection;
import com.kamar.issuemanagementsystem.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the department repository.
 * @author kamar baraka.*/

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String > {

    Optional<DepartmentDtoProjection> findDepartmentByDepartmentName(String departmentName);
}
