/*
package com.kamar.issuemanagementsystem.ticket.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

*/
/**
 * the solution to a ticket.
 * @author kamar baraka.*//*


@Entity(name = "solutions")
@Data
public class Solution {

    @Id
    private String ticketNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Ticket solutionTo;

    @Column(name = "solution", nullable = false)
    private String theSolution;

    @Column(name = "solved_on")
    private final LocalDateTime dateTimeSolved = LocalDateTime.now();
}
*/
