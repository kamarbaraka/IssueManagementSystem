package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * the comment entity.
 * @author kamar baraka.*/

@Entity
@Data
@Table(name = "comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String theComment;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "commented_to")
    private Ticket commentedTo;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "commented_by")
    private User commentedBy;

    @Column(nullable = false, updatable = false)
    private final LocalDate commentedOn = LocalDate.now();
}
