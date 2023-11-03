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

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "reffered_ticket", insertable = false, nullable = false)
    private Ticket refferedTicket;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_from", insertable = false, nullable = false)
    private User from;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_to", insertable = false, nullable = false)
    private User to;

    private boolean accepted = false;

    @Column(nullable = false, updatable = false)
    private final LocalDate requestedOn = LocalDate.now();
}
