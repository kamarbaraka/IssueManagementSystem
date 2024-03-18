/*
package com.kamar.issuemanagementsystem.department.data.projection;

import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;

*/
/**
 * the projection for department dto.
 * @author kamar baraka.*//*


public interface DepartmentDtoProjection extends DepartmentDtoType {

    String getDepartmentName();
    UserEntity getHeadOfDepartment();

    UserRating getRating();

    default String getHODAsString(){

        return getHeadOfDepartment().getUsername();
    }

    default int getTheRate(){

        return getRating().getRate();
    }

}
*/
