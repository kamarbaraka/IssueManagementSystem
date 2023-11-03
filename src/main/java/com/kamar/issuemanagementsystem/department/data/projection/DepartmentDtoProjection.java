package com.kamar.issuemanagementsystem.department.data.projection;

import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.ProjectedPayload;

/**
 * the projection for department dto.
 * @author kamar baraka.*/

public interface DepartmentDtoProjection extends DepartmentDtoType {

    String getDepartmentName();
    User getHeadOfDepartment();

    Rating getRating();

    default String getHODAsString(){

        return getHeadOfDepartment().getUsername();
    }

    default int getTheRate(){

        return getRating().getRate();
    }

}
