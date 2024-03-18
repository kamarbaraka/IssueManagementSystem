/*
package com.kamar.issuemanagementsystem.attachment.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

*/
/**
 * the attachments entity.
 * @author kamar baraka.*//*


@Entity(name = "attachments")
@Data
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id", nullable = false, updatable = false)
    private long id;

    private String filename;

    private String contentType;

    @Lob
    private Blob content;
}
*/
