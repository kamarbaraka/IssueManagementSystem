package com.kamar.issuemanagementsystem.user_management.data.dto;

import java.io.Serializable;

/**
 * activation result dto*/

public record ActivationSuccessDTO(

        String message
) implements DtoType, Serializable {
}
