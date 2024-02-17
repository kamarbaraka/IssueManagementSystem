package com.kamar.issuemanagementsystem.rating.data.dto;

/**
 * the department rating dto.
 * @author kamar baraka.*/

public record DepartmentRatingDto(

        String departmentName
        /*@Min(value = 0, message = "rating can't be negative")
        @Max(value = 5, message = "exceeds the rating limit")
        int rating*/
) implements RatingDtoType {
}
