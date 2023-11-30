package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * ticket entity.
 * @author kamar baraka.*/


@Entity(name = "tickets")
@Data
public class Ticket implements Serializable {

    @Transient
    private final Random random = new Random();

    @Id
    /*@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;*/

    @Column(name = "ticket_number", nullable = false, updatable = false, unique = true)
    private final String ticketNumber = "#T"+ random.nextLong(100000, 999999)+ "ims";

    @Size(max = 50, message = "title too long")
    @Column(nullable = false)
    private String title;

    @Size(max = 500)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attachments")
    private final Collection<Attachment> attachments = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "department_assigned")
    private Department departmentAssigned;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_to")
    private User assignedTo ;

    @FutureOrPresent(message = "deadline must be in the future.")
    private LocalDate deadline;

    @ManyToOne( cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "raised_by")
    private User raisedBy;

    @Column(nullable = false, updatable = false)
    private final LocalDate createdOn = LocalDate.now();
}
