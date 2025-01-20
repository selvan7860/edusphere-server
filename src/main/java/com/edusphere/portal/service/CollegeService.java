package com.edusphere.portal.service;

import com.edusphere.portal.dto.CollegeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CollegeService {

    CollegeDTO createCollege(CollegeDTO collegeDTO);

    CollegeDTO getCollege(String id);

    List<CollegeDTO> getAllColleges();

    void deleteCollege(String id);
}
