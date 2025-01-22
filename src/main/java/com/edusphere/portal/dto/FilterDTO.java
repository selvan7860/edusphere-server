package com.edusphere.portal.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterDTO {

    private String field;
    private String value;
}
