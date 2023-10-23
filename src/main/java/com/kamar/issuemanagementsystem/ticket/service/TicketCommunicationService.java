package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Reply;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.entity.User;

/**
 * service to handle ticket communication.
 * @author kamar baraka.*/

public interface TicketCommunicationService {

    void commentOnATicket(Comment comment, Ticket on, User by);
    void replyOnAComment(Reply reply, Comment on, User by);
}
