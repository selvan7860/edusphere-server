package com.edusphere.portal.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeSearchResponseDTO {
    private List<CollegeDTO> colleges;
    private List<String> locations;
    private List<String> courses;
}
