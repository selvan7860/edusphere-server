package com.edusphere.portal.elasticRepository;

import com.edusphere.portal.ElasticDao.CollegeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeElasticRepository extends ElasticsearchRepository<CollegeDocument, String> {
    void save(CollegeDocument collegeDocument);
}
