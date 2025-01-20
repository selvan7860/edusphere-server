package com.edusphere.portal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CollegeDTO {

    private String id;
    private String collegeName;
    private String collegeCode;
    private String collegeLocation;
    private String collegeEmail;
    private String collegePhone;
    private String collegeWebSiteLink;
    private String collegeDescription;

    private List<CourseDTO> courses;


    public CollegeDTO(String id, String collegeName, String collegeCode, String collegeLocation, String collegeEmail, String collegePhone, String collegeWebSiteLink, String collegeDescription, List<CourseDTO> courseDTOS) {
        this.id = id;
        this.collegeName = collegeName;
        this.collegeCode = collegeCode;
        this.collegeLocation = collegeLocation;
        this.collegeEmail = collegeEmail;
        this.collegePhone = collegePhone;
        this.collegeWebSiteLink = collegeWebSiteLink;
        this.collegeDescription = collegeDescription;
        this.courses = courseDTOS;
    }
}
