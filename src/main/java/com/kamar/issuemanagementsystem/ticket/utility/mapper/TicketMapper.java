package com.kamar.issuemanagementsystem.ticket.utility.mapper;

import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketCreationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;

/**
 * the ticket mapper.
 * @author kamar baraka.*/

public interface TicketMapper {

    Ticket dtoToEntity(TicketCreationDTO ticketCreationDTO);
    TicketAdminPresentationDTO entityToDTOAdmin(Ticket ticket);

}
