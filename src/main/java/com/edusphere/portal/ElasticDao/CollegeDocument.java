package com.edusphere.portal.ElasticDao;


import co.elastic.clients.elasticsearch.license.LicenseStatus;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "colleges")
@Data
@NoArgsConstructor
public class CollegeDocument {

    @Id
    private String id;
    private String collegeName;
    private String collegeCode;
    private String collegeLocation;
    private String collegeEmail;
    private String collegePhone;
    private String collegeWebSiteLink;
    private String collegeDescription;

    private List<CourseDocument> courses;
}
