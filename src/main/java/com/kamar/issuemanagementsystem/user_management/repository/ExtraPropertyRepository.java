package com.kamar.issuemanagementsystem.user_management.repository;

import com.kamar.issuemanagementsystem.user_management.entity.ExtraProperty;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

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
public interface ExtraPropertyRepository extends JpaRepository<ExtraProperty, Long> {

    @NotNull
    List<ExtraProperty> findExtraPropertiesByUserOrderById(@NotNull UserEntity userEntity);
    @NotNull
    Optional<ExtraProperty> findExtraPropertyByKeyIgnoreCaseAndUser(@NotNull String key, @NotNull UserEntity userEntity);
}