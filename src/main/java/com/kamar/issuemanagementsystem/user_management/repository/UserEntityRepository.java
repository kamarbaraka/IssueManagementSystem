package com.kamar.issuemanagementsystem.user_management.repository;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The UserRepository interface provides methods to interact with the user table in the database.
 * It extends the JpaRepository interface.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, String> {

    /*find a user by email*/
    Optional<UserEntity> findUserByUsername(String username);
    void deleteUserByUsername(String username);
    Optional<UserEntity> findUserByUsernameAndAuthoritiesContaining(String username, UserAuthority authority);
    List<UserEntity> findUserByAuthoritiesContaining(UserAuthority authority);
    boolean existsByUsername(String username);



}

