package com.kamar.issuemanagementsystem.ticket.exceptions;

import java.io.IOException;

/**
 * wrapper exception for ticket submissions.
 * @author kamar baraka.*/

public class TicketSubmissionException extends IOException {

    public TicketSubmissionException(String message) {
        super(message);
    }
}
