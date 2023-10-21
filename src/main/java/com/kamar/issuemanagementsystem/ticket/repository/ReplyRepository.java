package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Reply;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the reply repository.
 * @author kamar baraka.*/

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<List<Reply>> findRepliesByRepliedByOrderByRepliedOn(User user);

    Optional<List<Reply>> findRepliesByRepliedToOrderByRepliedOn(Comment comment);
}
