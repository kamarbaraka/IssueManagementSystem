package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * referral request entity.
 * @author kamar baraka.*/

@Entity(name = "referral_requests")
@Data
public class ReferralRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long requestId;

    @ManyToOne( cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "reffered_ticket", nullable = false)
    private Ticket refferedTicket;

    @Column(name = "reason")
    private String reason = "";

    @ManyToOne( cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "request_from", nullable = false)
    private User from;

    @ManyToOne( cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "request_to", nullable = false)
    private User to;

    private boolean accepted = false;

    @Column(nullable = false, updatable = false)
    private final LocalDate requestedOn = LocalDate.now();
}
