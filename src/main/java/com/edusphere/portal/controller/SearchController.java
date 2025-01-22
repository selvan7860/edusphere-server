package com.edusphere.portal.controller;

import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.CollegeSearchResponseDTO;
import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.dto.SearchDTO;
import com.edusphere.portal.service.CollegeService;
import com.edusphere.portal.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;
    private final CollegeService collegeService;

    public SearchController(SearchService searchService, CollegeService collegeService) {
        this.searchService = searchService;
        this.collegeService = collegeService;
    }

    @PostMapping
    public GenericResponse searchColleges(@RequestBody SearchDTO request){
        try{
           CollegeSearchResponseDTO colleges = searchService.searchColleges(request);
            return new GenericResponse(colleges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
