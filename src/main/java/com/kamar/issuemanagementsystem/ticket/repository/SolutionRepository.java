package com.kamar.issuemanagementsystem.ticket.repository;

import com.kamar.issuemanagementsystem.ticket.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * repository for the solution.
 * @author kamar baraka.*/

@Repository
public interface SolutionRepository  extends JpaRepository<Solution, String > {
}
