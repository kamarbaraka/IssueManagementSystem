package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the comment repository.
 * @author kamar baraka.*/

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findCommentsByCommentedToOrderByCommentedOn(Ticket ticket);

    Optional<List<Comment>> findCommentsByCommentedByOrderByCommentedOn(User user);
}
