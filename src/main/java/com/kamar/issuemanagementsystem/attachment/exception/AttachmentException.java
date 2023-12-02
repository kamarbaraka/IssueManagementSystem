package com.kamar.issuemanagementsystem.attachment.exception;

import java.io.IOException;

/**
 * attachment exception class.
 * @author kamar baraka.*/

public class AttachmentException extends IOException {

    public AttachmentException(String message) {
        super(message);
    }
}
