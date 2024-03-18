/*
package com.kamar.issuemanagementsystem.department.repository;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

*/
/**
 * the department repository.
 * @author kamar baraka.*//*


@Repository
public interface DepartmentRepository extends JpaRepository<Department, String > {

    Optional<Department> findDepartmentByDepartmentName(String departmentName);
    Optional<Department> findDepartmentByMembersContaining(UserEntity userEntity);
}
*/
