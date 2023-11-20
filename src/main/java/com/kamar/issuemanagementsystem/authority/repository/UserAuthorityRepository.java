package com.kamar.issuemanagementsystem.authority.repository;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.LinkOption;

/**
 * the user authority contract.
 * @author kamar baraka.*/

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
