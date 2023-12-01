package com.kamar.issuemanagementsystem.attachment.repository;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * the attachments repository.
 * @author kamar baraka.*/

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
