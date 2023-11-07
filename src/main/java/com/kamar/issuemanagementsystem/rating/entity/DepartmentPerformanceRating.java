package com.kamar.issuemanagementsystem.rating.entity;

import com.kamar.issuemanagementsystem.department.entity.Department;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.io.Serializable;

/**
 * the department performance rating.
 * @author kamar baraka.*/


@Entity
@Data
public class DepartmentPerformanceRating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false, insertable = false)
    private long id;

    /*@OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "department")
    private Department department;*/

    @Column(name = "no_of_members", nullable = false)
//    @Formula("select count(members) from departments d where d.department_name=department")
    private long numberOfMembers = 0;

    @Column(name = "total_member_rating", nullable = false)
//    @Formula("(SELECT SUM(u.user_rating.rate) FROM users u WHERE u.department_name = department)")
    private  long totalMemberRating = 0;

    @Column(name = "rating", nullable = false)
//    @Formula("(CASE WHEN number_of_members > 0 THEN total_member_rating / number_of_members ELSE 0 END)")
    private int rating = 0;
}
