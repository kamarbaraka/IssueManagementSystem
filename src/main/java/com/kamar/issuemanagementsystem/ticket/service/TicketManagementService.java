package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * the ticket management contract.
 * @author kamar baraka.*/

public interface TicketManagementService {

    Ticket updateTicket(Ticket ticket);
    Ticket getTicketById(final String  id) throws TicketException;
    List<Ticket> getTicketsByRaisedBy(User user);
    List<Attachment> downloadTicketAttachment(final String  ticketId) throws TicketException;
    List<TicketAdminPresentationDTO> getTicketsByDepartmentAndStatus(Department departmentAssigned, TicketStatus status);
    List<TicketAdminPresentationDTO> getTicketsByDepartment(Department departmentAssigned);
}
