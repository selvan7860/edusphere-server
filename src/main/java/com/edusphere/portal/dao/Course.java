package com.edusphere.portal.dao;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Data
public class Course {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

}
