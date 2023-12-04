package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * the comment entity.
 * @author kamar baraka.*/

@Entity(name = "comments")
@Data
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String theComment;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, targetEntity = Ticket.class)
    @PrimaryKeyJoinColumn(name = "ticket_number")
    private Ticket commentedTo;

    @Column(nullable = false, updatable = false)
    private final LocalDate commentedOn = LocalDate.now();
}
