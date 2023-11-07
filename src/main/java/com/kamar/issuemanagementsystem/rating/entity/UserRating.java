package com.kamar.issuemanagementsystem.rating.entity;

import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.io.Serializable;

/**
 * the rating entity.
 * @author kamar baraka.*/

@Entity(name = "ratings")
@Data
public class UserRating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private long ratingId;

    /*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            optional = false, orphanRemoval = true)
    @JoinColumn(name = "username")
    private User ratingFor;*/

    @Column(name = "number_of_ratings", nullable = false)
    private long numberOfRatings = 0;

    @Column(name = "total_rates", nullable = false)
    private long totalRates = 0;

//    @Formula("(CASE WHEN number_of_ratings > 0 THEN total_rates / number_of_ratings ELSE 0 END)")
    @Column(name = "rate", nullable = false)
    private int rate = 0;

}
