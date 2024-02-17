package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * comment reply entity.
 * @author kamar baraka.*/

@Entity(name = "replies")
@Data
public class Reply implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false)
    private String theReply;

    @ManyToOne(cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "replied_to",  updatable = false)
    private Comment repliedTo;

    @ManyToOne(cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "replied_by")
    private UserEntity repliedBy;

    @Column(nullable = false, updatable = false)
    private final LocalDate repliedOn = LocalDate.now();
}
