package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;

import java.util.List;

/**
 * the members dto.
 * @author kamar baraka.*/

public record MembersDto(
        String departmentName,
        List<String > members
) implements DtoType {
}
