package com.edusphere.portal.service.SearchServiceImpl;

import com.edusphere.portal.ElasticSearchConfiguration.ElasticSearchClient;
import com.edusphere.portal.dao.College;
import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.CollegeSearchResponseDTO;
import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.dto.SearchDTO;
import com.edusphere.portal.repository.CollegeRepository;
import com.edusphere.portal.service.SearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {


    private final ElasticSearchClient client;
    private final CollegeRepository collegeRepository;

    public SearchServiceImpl(ElasticSearchClient client, CollegeRepository collegeRepository) {
        this.client = client;
        this.collegeRepository = collegeRepository;
    }

    @Override
    public CollegeSearchResponseDTO searchColleges(SearchDTO request) {
        GenericResponse response = client.searchColleges(request);
        List<CollegeDTO> collegeDTOs = response.getColleges();
        List<String> locations = response.getLocations();
        List<String> courses = response.getCourses();
        return new CollegeSearchResponseDTO(collegeDTOs, locations, courses);
    }
}
