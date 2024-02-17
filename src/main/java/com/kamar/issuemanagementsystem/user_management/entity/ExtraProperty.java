package com.kamar.issuemanagementsystem.user_management.entity;

import com.kamar.issuemanagementsystem.user_management.dtos.ExtraPropertyDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;


/**
 * Represents an ExtraProperty entity that holds additional properties for a User.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "extra_props")
public class ExtraProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "key cannot be blank.")
    @NotEmpty(message = "key cannot be empty.")
    @NotNull(message = "key cannot be null.")
    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", nullable = false)
    private Object value;

    @Builder.Default
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn = Instant.now();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_user_id", nullable = false)
    private UserEntity userEntity;

    /**
     * Converts an ExtraPropertyDto object to an ExtraProperty entity object.
     *
     * @param dto The ExtraPropertyDto object to be converted.
     * @return The converted ExtraProperty entity object.
     */
    public static ExtraProperty dtoToEntity(ExtraPropertyDto dto){

        return ExtraProperty.builder()
                .key(dto.key())
                .value(dto.value())
                .build();
    }

}