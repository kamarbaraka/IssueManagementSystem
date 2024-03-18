/*
package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

*/
/**
 * the comment entity.
 * @author kamar baraka.*//*


@Entity(name = "comments")
@Data
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",nullable = false)
    private long commentId;

    @Column(nullable = false)
    private String theComment;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Ticket commentFor;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "commented_by", nullable = false)
    private UserEntity commentedBy;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private UserEntity commentedTo;

    @Column(nullable = false, updatable = false)
    private final LocalDate commentedOn = LocalDate.now();
}
*/
