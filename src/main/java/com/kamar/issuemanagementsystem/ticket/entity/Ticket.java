package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * ticket entity.
 * @author kamar baraka.*/


@Entity
@Data
@Table(name = "tickets")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @Size(max = 50, message = "title too long")
    private String title;

    @Size(max = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "raised_by")
    @Column(nullable = false, updatable = false)
    private User raisedBy;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    private LocalDate deadline;

    @Column(nullable = false, updatable = false)
    private final LocalDate createdOn = LocalDate.now();
}
