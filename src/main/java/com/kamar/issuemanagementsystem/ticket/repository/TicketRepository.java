/*
package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findTicketsByTitle(String title);
    List<Ticket> findTicketsByRaisedByOrderByCreatedOnAsc(UserEntity userEntity);
    List<Ticket> findTicketsByAssignedToOrderByDeadlineDesc(UserEntity userEntity);
    List<Ticket> findTicketsByStatusOrderByDeadline(TicketStatus status);
    List<Ticket> findTicketsByStatusOrderByCreatedOnAsc(TicketStatus status);
    List<Ticket> findTicketsByDepartmentAssignedAndStatusOrderByCreatedOnAsc(Department departmentAssigned, TicketStatus status);
    List<Ticket> findTicketsByDepartmentAssignedOrderByCreatedOnAsc(Department departmentAssigned);
    List<Ticket> findTicketsByAssignedToAndStatusOrderByCreatedOnAsc(UserEntity assignedTo, TicketStatus status);
    List<Ticket> findTicketsByPriorityAndStatus(TicketPriority priority, TicketStatus status);
}
*/
