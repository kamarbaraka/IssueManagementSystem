package com.kamar.issuemanagementsystem.rating.repository;

import com.kamar.issuemanagementsystem.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * the rating repository.
 * @author kamar baraka.*/

@Repository
public interface RatingRepository extends JpaRepository<Rating, String > {
}
