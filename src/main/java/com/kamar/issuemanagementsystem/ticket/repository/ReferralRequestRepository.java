package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * the referral request repository.
 * @author kamar baraka.*/

@Repository
public interface ReferralRequestRepository extends JpaRepository<ReferralRequest, Long> {

    List<ReferralRequest> findReferralRequestsByToOrderByRequestedOn(User to);

    List<ReferralRequest> findReferralRequestsByFromOrderByRequestedOn(User from);

    List<ReferralRequest> findReferralRequestsByToAndAcceptedOrderByRequestedOn(User to, boolean accepted);
    List<ReferralRequest> findReferralRequestsByFromAndAcceptedOrderByRequestedOn(User from, boolean accepted);
}
