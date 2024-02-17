package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.Comment;
import com.kamar.issuemanagementsystem.ticket.entity.Reply;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * the reply repository.
 * @author kamar baraka.*/

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findRepliesByRepliedByOrderByRepliedOnDesc(UserEntity userEntity);

    List<Reply> findRepliesByRepliedToOrderByRepliedOnDesc(Comment comment);
}
