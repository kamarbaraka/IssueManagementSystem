package com.kamar.issuemanagementsystem.ticket.utility.mapper;

import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketCreationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import org.springframework.stereotype.Service;

/**
 * implementation of the ticket mapper.
 * @author kamar baraka.*/

@Service
public class TicketMapperImpl implements TicketMapper {
    @Override
    public Ticket dtoToEntity(TicketCreationDTO ticketCreationDTO) {
        /*map the dto*/
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketCreationDTO.title());
        ticket.setDescription(ticketCreationDTO.description());

        return ticket;
    }

    @Override
    public TicketAdminPresentationDTO entityToDTOAdmin(Ticket ticket) {

        if (ticket.getAssignedTo() == null || ticket.getDeadline() == null){
            return new TicketAdminPresentationDTO(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getRaisedBy().getUsername(),
                "not assigned yet",
                "not assigned yet");
        }
        /*map the dto*/
        return new TicketAdminPresentationDTO(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getRaisedBy().getUsername(),
                ticket.getAssignedTo().getUsername(),
                ticket.getDeadline().toString()
        );

    }
}
