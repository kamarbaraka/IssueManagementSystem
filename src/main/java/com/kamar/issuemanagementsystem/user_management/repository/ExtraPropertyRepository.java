package com.kamar.issuemanagementsystem.user_management.repository;

import com.kamar.issuemanagementsystem.user_management.entity.ExtraProperty;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a repository for managing ExtraProperty entities.
 * It extends the JpaRepository interface, specifying the ExtraProperty entity class and the type of its primary key.
 *
 * @see JpaRepository
 * @see ExtraProperty
 * @see Long
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */

@Repository
public interface ExtraPropertyRepository extends JpaRepository<ExtraProperty, Long> {

    /**
     * Finds and returns a list of ExtraProperties ordered by their ids for a given UserEntity.
     *
     * @param userEntity The UserEntity object for which to find the ExtraProperties.
     * @return A list of ExtraProperties.
     * @throws NullPointerException if userEntity is null.
     */
    @NotNull
    List<ExtraProperty> findExtraPropertiesByUserEntityOrderById(@NotNull UserEntity userEntity);
    /**
     * Finds and returns an ExtraProperty with the specified key (case-insensitive) and UserEntity.
     *
     * @param key        The key of the ExtraProperty.
     * @param userEntity The UserEntity for which to find the ExtraProperty.
     * @return An Optional containing the ExtraProperty if found, or an empty Optional if not found.
     * @throws NullPointerException if key or userEntity is null.
     */
    @NotNull
    Optional<ExtraProperty> findExtraPropertyByKeyIgnoreCaseAndUserEntity(@NotNull String key, @NotNull UserEntity userEntity);
}