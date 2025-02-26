package com.edusphere.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseDTO {

    private String id;
    private String courseName;
    private String seatAvailable;

    public CourseDTO(String id, String courseName, String seatAvailable) {
        this.id = id;
        this.courseName = courseName;
        this.seatAvailable = seatAvailable;
    }
}
