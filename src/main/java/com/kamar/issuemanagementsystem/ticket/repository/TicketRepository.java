package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findTicketsByTitle(String title);
    List<Ticket> findTicketsByRaisedByOrderByCreatedOnAsc(User user);
    List<Ticket> findTicketsByAssignedToOrderByDeadlineDesc(User user);
    List<Ticket> findTicketsByStatusOrderByDeadline(TicketStatus status);
    List<Ticket> findTicketsByStatusOrderByCreatedOnAsc(TicketStatus status);
    List<Ticket> findTicketsByDepartmentAssignedAndStatusOrderByCreatedOnAsc(Department departmentAssigned, TicketStatus status);
    List<Ticket> findTicketsByDepartmentAssignedOrderByCreatedOnAsc(Department departmentAssigned);
    List<Ticket> findTicketsByAssignedToAndStatusOrderByCreatedOnAsc(User assignedTo, TicketStatus status);
    List<Ticket> findTicketsByPriorityAndStatus(TicketPriority priority, TicketStatus status);
}
