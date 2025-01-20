package com.edusphere.portal.controller;

import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.service.CollegeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/colleges")
public class CollegeController {

    private final CollegeService collegeService;


    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping
    public GenericResponse createCollege(@RequestBody CollegeDTO collegeDTO){
        CollegeDTO createCollege = collegeService.createCollege(collegeDTO);
        return new GenericResponse(createCollege);
    }

    @GetMapping
    public GenericResponse getCollege(){
        return new GenericResponse(collegeService.getAllColleges());
    }
}
