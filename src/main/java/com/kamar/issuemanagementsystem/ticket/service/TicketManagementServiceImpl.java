package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.utility.util.UserUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * implementation of the ticket management.\
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class TicketManagementServiceImpl implements TicketManagementService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final DepartmentRepository departmentRepository;
    private final UserUtilityService userUtilityService;

    @Override
    public Ticket updateTicket(Ticket ticket) {

        /*check if exists*/
        Ticket savedTicket = ticketRepository.findById(ticket.getTicketNumber()).orElseThrow();
        savedTicket.setAssignedTo(ticket.getAssignedTo());
        savedTicket.setPriority(ticket.getPriority());
        savedTicket.setDeadline(ticket.getDeadline());
        savedTicket.setStatus(TicketStatus.ASSIGNED);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(String  id) throws TicketException {

        /*get the ticket by ticketNumber*/
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketException("no such ticket"));

        /*get authenticated user*/
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /*get the user*/
        UserEntity userEntity = (UserEntity) authenticatedUser;

        /*filter the ticket for user*/
        if (userUtilityService.hasAuthority(authenticatedUser, "user")) {
            /*check if user raised the ticket*/
            if (ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())) {
                return ticket;
            }else throw new TicketException("not authorized to access the resource");
        }

        /*filter for employee*/
        if (userUtilityService.hasAuthority(authenticatedUser, "employee")) {
            /*check if the user is assigned the ticket*/
            if (ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername()) ||
                    ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())) {
                return ticket;
            }else throw new TicketException("unauthorized to access");
        }

        /*filter for department admin*/
        if (userUtilityService.hasAuthority(authenticatedUser, "department_admin")) {
            /*check if the ticket belongs to his department*/
            Department department = departmentRepository.findDepartmentByMembersContaining(userEntity).orElseThrow(
                    () -> new TicketException("user doesn't belong to a department"));
            if (ticket.getDepartmentAssigned().equals(department)) {
                return ticket;
            }else throw new TicketException("access unauthorized");
        }

        /*filter for admin and owner*/
        return ticket;
    }

    @Override
    public List<Ticket> getTicketsByRaisedBy(UserEntity userEntity) {

        /*get tickets*/
        return ticketRepository.findTicketsByRaisedByOrderByCreatedOnAsc(userEntity);
    }

    public List<Attachment> downloadTicketAttachment(final String ticketId) throws TicketException{

        /*get the ticket*/
        Optional<Ticket> optSavedTicket = ticketRepository.findById(ticketId);

        if (optSavedTicket.isEmpty()) {
            throw new TicketException("ticket not found");
        }

        Ticket ticket = optSavedTicket.get();

        /*check if the ticket has attachments*/
        if (ticket.getAttachments().isEmpty()) {
            /*throw*/
            throw new TicketException("ticket has no attachments to download");
        }

        Collection<Attachment> attachments = ticket.getAttachments();
        return new ArrayList<>(attachments);
    }

    @Override
    public List<TicketAdminPresentationDTO> getTicketsByDepartmentAndStatus(Department departmentAssigned, TicketStatus  status) {


        /*get the tickets*/
        List<Ticket> departmentTickets = ticketRepository.findTicketsByDepartmentAssignedAndStatusOrderByCreatedOnAsc(departmentAssigned, status);
        /*map to dto*/
        return departmentTickets.stream().map(ticketMapper::entityToDTOAdmin).toList();
    }

    @Override
    public List<TicketAdminPresentationDTO> getTicketsByDepartment(Department departmentAssigned) {

        /*get the tickets*/
        List<Ticket> departmentTickets = ticketRepository.findTicketsByDepartmentAssignedOrderByCreatedOnAsc(departmentAssigned);
        /*map to dto*/
        return departmentTickets.stream().map(ticketMapper::entityToDTOAdmin).toList();
    }
}
