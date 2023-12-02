package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * the comment repository.
 * @author kamar baraka.*/

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByCommentedToOrderByCommentedOnAsc(Ticket ticket);

    List<Comment> findCommentsByCommentedByOrderByCommentedOnAsc(User user);
}
