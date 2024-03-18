/*
package com.kamar.issuemanagementsystem.ticket.utility.mapper;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.exception.AttachmentException;
import com.kamar.issuemanagementsystem.attachment.utils.AttachmentMapper;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketCreationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Solution;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.repository.CommentRepository;
import com.kamar.issuemanagementsystem.ticket.repository.SolutionRepository;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

*/
/**
 * implementation of the ticket mapper.
 * @author kamar baraka.*//*


@Service
@RequiredArgsConstructor
public class TicketMapperImpl implements TicketMapper {

    private final AttachmentMapper attachmentMapper;
    private final SolutionRepository solutionRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Ticket dtoToEntity(TicketCreationDTO ticketCreationDTO) {

        */
/*get the department to assign to ticket*//*

        Department department = departmentRepository.findDepartmentByDepartmentName(
                ticketCreationDTO.departmentToAssign()).orElseThrow();
        */
/*map the dto*//*

        Ticket ticket = new Ticket();
        ticket.setTitle(ticketCreationDTO.title());
        ticket.setDescription(ticketCreationDTO.description());
        ticket.setDepartmentAssigned(department);

        */
/*check for attachments*//*

        if (!ticketCreationDTO.attachments().isEmpty()) {

            */
/*enumerate the attachments*//*

            List<Attachment> attachments = ticketCreationDTO.attachments().stream()
                    .map(multipartFile -> {
                        try {
                            return attachmentMapper.multipartToAttachment(multipartFile);
                        } catch (AttachmentException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();

            */
/*add attachments to ticket*//*

            ticket.getAttachments().addAll(attachments);
        }

        return ticket;
    }

    @Override
    public TicketAdminPresentationDTO entityToDTOAdmin(Ticket ticket)  {

        */
/*get attachments*//*

        */
/*check if ticket has attachments and create a download link if present*//*

        boolean hasAttachments = !ticket.getAttachments().isEmpty();

        if (ticket.getDeadline() == null){
            return new TicketAdminPresentationDTO(
                    ticket.getTicketNumber(),
                ticket.getTitle(),
                ticket.getDescription(),
                "pending...",
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getRaisedBy().getUsername(),
                ticket.getDepartmentAssigned().getDepartmentName(),
                "not assigned yet",
                "not assigned yet",
                    hasAttachments
                    );
        }

        Optional<Solution> optSolution = solutionRepository.findById(ticket.getTicketNumber());

        if (optSolution.isEmpty()) {

            */
/*map the dto*//*

            return new TicketAdminPresentationDTO(
                    ticket.getTicketNumber(),
                    ticket.getTitle(),
                    ticket.getDescription(),
                    "pending...",
                    ticket.getPriority().toString(),
                    ticket.getStatus().toString(),
                    ticket.getRaisedBy().getUsername(),
                    ticket.getDepartmentAssigned().getDepartmentName(),
                    ticket.getAssignedTo().getUsername(),
                    ticket.getDeadline().toString(),
                    hasAttachments
            );
        }

        Solution solution = optSolution.get();
        */
/*map the dto*//*

        return new TicketAdminPresentationDTO(
                ticket.getTicketNumber(),
                ticket.getTitle(),
                ticket.getDescription(),
                solution.getTheSolution(),
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getRaisedBy().getUsername(),
                ticket.getDepartmentAssigned().getDepartmentName(),
                ticket.getAssignedTo().getUsername(),
                ticket.getDeadline().toString(),
                hasAttachments
        );

    }
}
*/
