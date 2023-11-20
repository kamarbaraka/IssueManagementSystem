package com.kamar.issuemanagementsystem.user.repository;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * the user repository.
 * @author kamar baraka.*/

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /*find a user by email*/
    Optional<User> findUserByUsername(String username);
    void deleteUserByUsername(String username);
    Optional<User> findUserByUsernameAndAuthoritiesContaining(String username, UserAuthority authority);
    List<User> findUserByAuthoritiesContaining(UserAuthority authority);
    boolean existsByUsername(String username);

}

