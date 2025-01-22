package com.edusphere.portal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchDTO {

    private String q;
    private List<FilterDTO> filter;
}
