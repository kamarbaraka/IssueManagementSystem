package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.utility.util.UserUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * implementation of the ticket reporting service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class TicketReportingServiceImpl implements TicketReportingService {

    private final TicketRepository ticketRepository;
    private final UserAuthorityUtility userAuthorityUtility;
    private final UserUtilityService userUtilityService;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Ticket> ticketsByStatus(TicketStatus status, UserDetails authenticatedUser) {

        /*get tickets by status*/
        List<Ticket> ticketsByStatus = ticketRepository.findTicketsByStatusOrderByCreatedOn(status).orElseThrow();

        /*filter for employee*/
        if (userUtilityService.hasAuthority(authenticatedUser, "employee")) {

            /*filter the result*/
            return ticketsByStatus.parallelStream().filter(
                    ticket -> ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }

        return ticketsByStatus;
    }

    @Override
    public List<Ticket> userTicketsByStatus(User user, TicketStatus ticketStatus) {

        /*get tickets*/
        return ticketRepository.findTicketsByAssignedToAndStatus(user, ticketStatus).orElseThrow();
    }

    @Override
    public List<Ticket> getAllTickets(UserDetails authenticatedUser) {

        /*get all tickets*/
        List<Ticket> allTickets = ticketRepository.findAll();

        /*filter for department admin*/
        if (userUtilityService.hasAuthority(authenticatedUser, "department_admin")) {
            /*get the user and department*/
            User user = userRepository.findUserByUsername(authenticatedUser.getUsername()).orElseThrow();
            Department department = departmentRepository.findDepartmentByMembersContaining(user).orElseThrow();

            return allTickets.stream().filter(ticket -> ticket.getDepartmentAssigned().equals(department)).toList();
        }

        /*filter for employee*/
        if (userUtilityService.hasAuthority(authenticatedUser, "employee")) {

            /*filter the tickets*/
            return allTickets.parallelStream().filter(
                    ticket -> ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername()) ||
                            ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }

        /*filter for users*/
        if (userUtilityService.hasAuthority(authenticatedUser, "user")) {

            /*filter*/
            return allTickets.parallelStream().filter(
                    ticket -> ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }


        return allTickets;

    }
}
