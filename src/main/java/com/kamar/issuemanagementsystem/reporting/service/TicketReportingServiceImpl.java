/*
package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import com.kamar.issuemanagementsystem.user_management.utility.util.UserUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

*/
/**
 * implementation of the ticket reporting service.
 * @author kamar baraka.*//*


@Service
@RequiredArgsConstructor
@Transactional
public class TicketReportingServiceImpl implements TicketReportingService {

    private final TicketRepository ticketRepository;
    private final UserUtilityService userUtilityService;
    private final DepartmentRepository departmentRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public List<Ticket> ticketsByStatus(TicketStatus status) {

        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        */
/*get tickets by status*//*

        List<Ticket> ticketsByStatus = ticketRepository.findTicketsByStatusOrderByCreatedOnAsc(status);

        */
/*filter for employee*//*

        if (userUtilityService.hasAuthority(authenticatedUser, "employee")) {

            */
/*filter the result*//*

            return ticketsByStatus.stream().filter(
                    ticket -> ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }

        return ticketsByStatus;
    }

    @Override
    public List<Ticket> userTicketsByStatus(UserEntity userEntity, TicketStatus ticketStatus) {

        */
/*get tickets*//*

        return ticketRepository.findTicketsByAssignedToAndStatusOrderByCreatedOnAsc(userEntity, ticketStatus);
    }

    @Override
    public List<Ticket> getAllTickets() {

        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        */
/*get all tickets*//*

        List<Ticket> allTickets = ticketRepository.findAll();

        */
/*filter for department admin*//*

        if (userUtilityService.hasAuthority(authenticatedUser, "department_admin")) {
            */
/*get the user and department*//*

            UserEntity userEntity = userEntityRepository.findUserByUsername(authenticatedUser.getUsername()).orElseThrow();
            Department department = departmentRepository.findDepartmentByMembersContaining(userEntity).orElseThrow();

            return allTickets.stream().filter(ticket -> ticket.getDepartmentAssigned().equals(department)).toList();
        }

        */
/*filter for employee*//*

        if (userUtilityService.hasAuthority(authenticatedUser, "employee")) {

            */
/*filter the tickets*//*

            return allTickets.stream().filter(
                    ticket -> ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername()) ||
                            ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }

        */
/*filter for users*//*

        if (userUtilityService.hasAuthority(authenticatedUser, "user")) {

            */
/*filter*//*

            return allTickets.stream().filter(
                    ticket -> ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }


        return allTickets;

    }
}
*/
