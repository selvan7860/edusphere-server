package com.edusphere.portal.service.CollegeServiceImpl;

import com.edusphere.portal.ElasticDao.CollegeDocument;
import com.edusphere.portal.ElasticDao.CourseDocument;
import com.edusphere.portal.dao.College;
import com.edusphere.portal.dao.Course;
import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.CourseDTO;
import com.edusphere.portal.elasticRepository.CollegeElasticRepository;
import com.edusphere.portal.repository.CollegeRepository;
import com.edusphere.portal.repository.CourseRepository;
import com.edusphere.portal.service.CollegeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepository collegeRepository;
    private final CollegeElasticRepository collegeElasticRepository;
    private final CourseRepository courseRepository;

    public CollegeServiceImpl(CollegeRepository collegeRepository, CollegeElasticRepository collegeElasticRepository, CourseRepository courseRepository) {
        this.collegeRepository = collegeRepository;
        this.collegeElasticRepository = collegeElasticRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public CollegeDTO createCollege(CollegeDTO collegeDTO) {
        College college = toEntity(collegeDTO);
        College savedCollege = collegeRepository.save(college);
        CollegeDocument collegeDocument = toDocument(savedCollege);
        collegeElasticRepository.save(collegeDocument);
        return toDto(savedCollege);
    }


    @Override
    public CollegeDTO getCollege(String id) {
        return null;
    }

    @Override
    public List<CollegeDTO> getAllColleges() {
        List<College> colleges = collegeRepository.findAll();
        if (colleges.isEmpty()) {
            throw new RuntimeException("No colleges found");
        }

        // Convert List<College> to List<CollegeDTO>
        return colleges.stream()
                .map(this::mapToCollegeDTO)
                .collect(Collectors.toList());
    }

    private CollegeDTO mapToCollegeDTO(College college) {
        return new CollegeDTO(
                college.getId(),
                college.getCollegeName(),
                college.getCollegeCode(),
                college.getCollegeLocation(),
                college.getCollegeEmail(),
                college.getCollegePhone(),
                college.getCollegeWebSiteLink(),
                college.getCollegeDescription(),
                mapCoursesToDTO(college.getCourses())
        );
    }

    private List<CourseDTO> mapCoursesToDTO(List<Course> courses) {
        return courses.stream()
                .map(this::mapToCourseDTO)
                .collect(Collectors.toList());
    }

    private CourseDTO mapToCourseDTO(Course course) {
        return new CourseDTO(course.getId(), course.getCourseName(), course.getSeatAvailable());
    }

    @Override
    public void deleteCollege(String id) {

    }

    private College toEntity(CollegeDTO collegeDTO) {
        College college = new College();
        college.setCollegeName(collegeDTO.getCollegeName());
        college.setCollegeEmail(collegeDTO.getCollegeEmail());
        college.setCollegeCode(collegeDTO.getCollegeCode());
        college.setCollegeLocation(collegeDTO.getCollegeLocation());
        college.setCollegePhone(collegeDTO.getCollegePhone());
        college.setCollegeWebSiteLink(collegeDTO.getCollegeWebSiteLink());
        college.setCollegeDescription(collegeDTO.getCollegeDescription());

        // Initialize courses if not null
        if(collegeDTO.getCourses() != null) {
            List<Course> courses = collegeDTO.getCourses().stream().map(courseDTO -> {
                Course course = new Course();
                course.setCourseName(courseDTO.getCourseName());
                course.setSeatAvailable(courseDTO.getSeatAvailable());
                course.setCollege(college);  // Set the parent college to course
                return course;
            }).collect(Collectors.toList());
            college.setCourses(courses);
        } else {
            college.setCourses(new ArrayList<>());  // Set empty list if courses are null
        }
        return college;
    }

    private CollegeDocument toDocument(College savedCollege) {
        CollegeDocument collegeDocument = new CollegeDocument();
        collegeDocument.setId(savedCollege.getId());
        collegeDocument.setCollegeName(savedCollege.getCollegeName());
        collegeDocument.setCollegeCode(savedCollege.getCollegeCode());
        collegeDocument.setCollegeLocation(savedCollege.getCollegeLocation());
        collegeDocument.setCollegeEmail(savedCollege.getCollegeEmail());
        collegeDocument.setCollegePhone(savedCollege.getCollegePhone());
        collegeDocument.setCollegeWebSiteLink(savedCollege.getCollegeWebSiteLink());
        collegeDocument.setCollegeDescription(savedCollege.getCollegeDescription());

        // Map courses from College entity to CollegeDocument
        if(savedCollege.getCourses() != null) {
            collegeDocument.setCourses(savedCollege.getCourses().stream().map(course -> {
                CourseDocument courseDocument = new CourseDocument();
                courseDocument.setId(course.getId());
                courseDocument.setCourseName(course.getCourseName());
                courseDocument.setSeatAvailable(course.getSeatAvailable());
                return courseDocument;
            }).collect(Collectors.toList()));
        }
        return collegeDocument;
    }

    private CollegeDTO toDto(College savedCollege) {
        CollegeDTO dto = new CollegeDTO();
        dto.setId(savedCollege.getId());
        dto.setCollegeName(savedCollege.getCollegeName());
        dto.setCollegeCode(savedCollege.getCollegeCode());
        dto.setCollegeLocation(savedCollege.getCollegeLocation());
        dto.setCollegeEmail(savedCollege.getCollegeEmail());
        dto.setCollegePhone(savedCollege.getCollegePhone());
        dto.setCollegeWebSiteLink(savedCollege.getCollegeWebSiteLink());
        dto.setCollegeDescription(savedCollege.getCollegeDescription());

        // Map courses from College entity to CollegeDTO
        if(savedCollege.getCourses() != null) {
            dto.setCourses(savedCollege.getCourses().stream().map(course -> {
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setId(course.getId());
                courseDTO.setCourseName(course.getCourseName());
                courseDTO.setSeatAvailable(course.getSeatAvailable());
                return courseDTO;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

}
