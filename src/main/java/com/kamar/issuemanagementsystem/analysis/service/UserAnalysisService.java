package com.kamar.issuemanagementsystem.analysis.service;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;

/**
 * the user analysis service.
 * @author kamar baraka.*/

public interface UserAnalysisService {

    UserPresentationDTO bestPerformantEmployee()throws AnalysisException;
    UserPresentationDTO mostPerformantEmployee() throws AnalysisException;

    UserPresentationDTO leastPerformantEmployee() throws AnalysisException;
}
