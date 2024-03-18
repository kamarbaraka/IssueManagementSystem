/*
package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

*/
/**
 * the comment repository.
 * @author kamar baraka.*//*


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findCommentByCommentedToOrderByCommentedOnAsc(Ticket ticket);
    Optional<Comment> findCommentByCommentedTo(Ticket commentedTo);

}
*/
