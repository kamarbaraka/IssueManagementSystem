package com.kamar.issuemanagementsystem.ticket.exceptions;

import java.io.IOException;

/**
 * all exceptions arising from the referral request.
 * @author kamar baraka.*/

public class ReferralRequestException extends IOException {

    public ReferralRequestException(String message) {
        super(message);
    }
}
