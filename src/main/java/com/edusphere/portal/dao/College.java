package com.edusphere.portal.dao;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ListIndexBase;

import java.util.List;

@Entity
@Table(name = "colleges")
@NoArgsConstructor
@Data
public class College {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String collegeName;
    private String collegeCode;
    private String collegeLocation;
    private String collegeEmail;
    private String collegePhone;
    private String collegeWebSiteLink;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;
}
