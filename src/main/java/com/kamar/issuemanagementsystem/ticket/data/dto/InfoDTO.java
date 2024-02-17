package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user_management.data.dto.DtoType;

/**
 * DTO for simple information.
 * @author kamar baraka.*/

public record InfoDTO(

        String info
) implements DtoType {
}
