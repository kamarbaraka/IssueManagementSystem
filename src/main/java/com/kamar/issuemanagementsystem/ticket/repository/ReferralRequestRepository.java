package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the referral request repository.
 * @author kamar baraka.*/

@Repository
public interface ReferralRequestRepository extends JpaRepository<ReferralRequest, Long> {

    Optional<List<ReferralRequest>> findReferralRequestByToOrderByRequestedOn(User to);

    Optional<List<ReferralRequest>> findReferralRequestByFromOrderByRequestedOn(User from);

    Optional<List<ReferralRequest>> findReferralRequestByToAndAcceptedOrderByRequestedOn(User to, boolean accepted);
    Optional<List<ReferralRequest>> findReferralRequestByFromAndAcceptedOrderByRequestedOn(User from, boolean accepted);
}
