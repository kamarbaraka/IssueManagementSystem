package com.kamar.issuemanagementsystem.user.repository;

import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * the user repository.
 * @author kamar baraka.*/

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /*find a user by email*/
    Optional<User> findUserByUsername(String username);
    void deleteUserByUsername(String username);

}
