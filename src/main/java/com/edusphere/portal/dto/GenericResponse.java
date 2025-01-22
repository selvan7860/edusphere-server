package com.edusphere.portal.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {

    private String message;
    private String errorMessage;
    private  int code;
    private Object data;
    private Boolean success;
    private List<CollegeDTO> colleges;
    private List<String> locations;
    private List<String> courses;

    public GenericResponse(boolean success,int code, String message) {
        this.code = code;
        this.message=message;
        this.success=success;
    }

    public GenericResponse(boolean success,int code, String message, String errorMessage) {
        this.message = message;
        this.errorMessage = errorMessage;
        this.code = code;
        this.success = success;
    }

    public GenericResponse(Object data) {
        this.data = data;
        this.code = 200;
        this.message = "Success";
        this.success=true;
    }

    public GenericResponse(List<CollegeDTO> colleges, List<String> locations, List<String> courses) {
        this.colleges = colleges;
        this.locations = locations;
        this.courses = courses;
    }
}
