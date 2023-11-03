package com.kamar.issuemanagementsystem.user.repository;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * the user repository.
 * @author kamar baraka.*/

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /*find a user by email*/
    Optional<User> findUserByUsername(String username);
    void deleteUserByUsername(String username);

    Optional<List<User>> findUsersByAuthorityOrderByCreatedOn(Authority authority);

    Optional<User> findUserByUsernameAndAuthority(String username, Authority authority);
    List<User> findUsersByAuthority(Authority authority);
    List<User> findUsersByAuthorityOrderByCreatedOnAsc(Authority authority);
    boolean existsByUsername(String username);
}
