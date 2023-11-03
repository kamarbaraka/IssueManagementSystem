package com.kamar.issuemanagementsystem.rating.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.io.Serializable;

/**
 * the rating entity.
 * @author kamar baraka.*/

@Entity(name = "ratings")
@Data
public class Rating implements Serializable {

    @Id
    @Column(nullable = false, updatable = false, name = "rating_for")
    private String ratingFor = "INIT";

    @Column(name = "number_of_ratings", nullable = false)
    private long numberOfRatings = 1;

    @Column(name = "total_rates", nullable = false)
    private long totalRates = 1;

    @Max(value = 5, message = "exceeds max rating")
    @Min(value = 0, message = "can't be negative")
    @Formula("(total_rates/number_of_ratings)")
    @Column(name = "rate", nullable = false)
    private final int rate = 1;

}
