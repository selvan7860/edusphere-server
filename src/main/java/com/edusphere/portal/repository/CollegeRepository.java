package com.edusphere.portal.repository;

import com.edusphere.portal.dao.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface CollegeRepository extends JpaRepository<College, String> {
}
