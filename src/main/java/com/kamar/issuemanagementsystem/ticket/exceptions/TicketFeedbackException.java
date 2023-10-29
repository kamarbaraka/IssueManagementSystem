package com.kamar.issuemanagementsystem.ticket.exceptions;

import java.io.IOException;

/**
 * exception for ticket feedback.
 * @author kamar baraka.*/

public class TicketFeedbackException extends IOException {

    public TicketFeedbackException(String message) {
        super(message);
    }
}
