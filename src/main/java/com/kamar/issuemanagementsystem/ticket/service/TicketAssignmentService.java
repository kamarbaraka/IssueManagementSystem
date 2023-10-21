package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;

/**
 * ticket assignment service.
 * @author kamar baraka.*/

public interface TicketAssignmentService {

    void assignTo(Ticket ticket);
    void referTicketTo( Ticket ticket, String to);
    void respondToReferralRequest(ReferralRequest referralRequest, boolean response);

}
