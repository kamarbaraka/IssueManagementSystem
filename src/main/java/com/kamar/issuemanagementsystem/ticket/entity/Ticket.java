/*
package com.kamar.issuemanagementsystem.ticket.entity;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.generator.TicketNumberGenerator;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

*/
/**
 * ticket entity.
 * @author kamar baraka.*//*



@Entity(name = "tickets")
@Data
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TicketNumberGenerator")
    @GenericGenerator(
            name = "TicketNumberGenerator",
            type = TicketNumberGenerator.class
    )
    @Column(name = "ticket_number", nullable = false, updatable = false, unique = true)
    private String ticketNumber;

    @Size(max = 50, message = "title too long")
    @Column(nullable = false)
    private String title;

    @Size(max = 500)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attachments")
    private final Collection<Attachment> attachments = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "department_assigned")
    private Department departmentAssigned;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    @ManyToOne( cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "assigned_to")
    private UserEntity assignedTo ;

    @FutureOrPresent(message = "deadline must be in the future.")
    private LocalDate deadline;

    @ManyToOne( cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            optional = false)
    @JoinColumn(name = "raised_by")
    private UserEntity raisedBy;

    @Column(nullable = false, updatable = false)
    private final LocalDate createdOn = LocalDate.now();
}
*/
