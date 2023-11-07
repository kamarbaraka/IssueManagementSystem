package com.kamar.issuemanagementsystem.department.data.projection;

import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.user.entity.User;

/**
 * the projection for department dto.
 * @author kamar baraka.*/

public interface DepartmentDtoProjection extends DepartmentDtoType {

    String getDepartmentName();
    User getHeadOfDepartment();

    UserRating getRating();

    default String getHODAsString(){

        return getHeadOfDepartment().getUsername();
    }

    default int getTheRate(){

        return getRating().getRate();
    }

}
