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

    Optional<List<Ticket>> findTicketsByTitle(String title);
    Optional<List<Ticket>> findTicketsByRaisedBy(User user);
    Optional<List<Ticket>> findTicketsByAssignedToOrderByDeadlineDesc(User user);
    Optional<List<Ticket>> findTicketsByStatusOrderByDeadline(TicketStatus status);
    Optional<List<Ticket>> findTicketsByStatusOrderByCreatedOn(TicketStatus status);
    List<Ticket> findTicketsByDepartmentAssignedAndStatus(Department departmentAssigned, TicketStatus status);
    List<Ticket> findTicketsByDepartmentAssigned(Department departmentAssigned);
    Optional<List<Ticket>> findTicketsByAssignedToAndStatus(User assignedTo, TicketStatus status);
    Optional<List<Ticket>> findTicketsByPriorityAndStatus(TicketPriority priority, TicketStatus status);
}
