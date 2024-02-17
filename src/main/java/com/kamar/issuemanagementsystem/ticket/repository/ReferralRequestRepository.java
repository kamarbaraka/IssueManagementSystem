package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the referral request repository.
 * @author kamar baraka.*/

@Repository
public interface ReferralRequestRepository extends JpaRepository<ReferralRequest, Long> {

    List<ReferralRequest> findReferralRequestsByToOrderByRequestedOn(UserEntity to);

    List<ReferralRequest> findReferralRequestsByFromOrderByRequestedOn(UserEntity from);

    List<ReferralRequest> findReferralRequestsByToAndAcceptedOrderByRequestedOn(UserEntity to, boolean accepted);
    List<ReferralRequest> findReferralRequestsByFromAndAcceptedOrderByRequestedOn(UserEntity from, boolean accepted);

    Optional<ReferralRequest> findReferralRequestsByRefferedTicket(Ticket referredTicket);
}
