package com.edusphere.portal.service;

import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.CollegeSearchResponseDTO;
import com.edusphere.portal.dto.SearchDTO;

import java.util.List;

public interface SearchService {

    CollegeSearchResponseDTO searchColleges(SearchDTO request);
}
