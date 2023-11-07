package com.kamar.issuemanagementsystem.rating.repository;

import com.kamar.issuemanagementsystem.rating.entity.DepartmentPerformanceRating;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * the department performance rating repository.
 * @author kamar baraka.*/


@Repository
public interface DepartmentPerformanceRatingRepository extends JpaRepository<DepartmentPerformanceRating, Long> {
}
