package com.edusphere.portal.ElasticSearchConfiguration;


import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.CourseDTO;
import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.dto.SearchDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ElasticSearchClient {

    @Autowired
    private RestHighLevelClient client;

    public GenericResponse searchColleges(SearchDTO searchRequest) {
        List<CollegeDTO> collegeList = new ArrayList<>();
        List<String> locations = new ArrayList<>();
        List<String> courses = new ArrayList<>();

        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            if ((searchRequest.getQ() == null || searchRequest.getQ().isEmpty()) &&
                    (searchRequest.getFilter() == null || searchRequest.getFilter().isEmpty())) {
                sourceBuilder.query(QueryBuilders.matchAllQuery());
            } else {
                if (searchRequest.getQ() != null && !searchRequest.getQ().isEmpty()) {
                    MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("collegeName", searchRequest.getQ());
                    sourceBuilder.query(matchQuery);
                }
                if (searchRequest.getFilter() != null && !searchRequest.getFilter().isEmpty()) {
                    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                    searchRequest.getFilter().forEach(filter -> {
                        boolQuery.filter(QueryBuilders.termQuery(filter.getField(), filter.getValue()));
                    });
                    sourceBuilder.query(boolQuery);
                }
            }
            AggregationBuilder locationAgg = AggregationBuilders.terms("locationAgg").field("collegeLocation.keyword");
            AggregationBuilder courseAgg = AggregationBuilders.nested("coursesAgg", "courses")
                    .subAggregation(AggregationBuilders.terms("courseNameAgg").field("courses.courseName"));
            sourceBuilder.aggregation(locationAgg);
            sourceBuilder.aggregation(courseAgg);
            SearchRequest esRequest = new SearchRequest("colleges");
            esRequest.source(sourceBuilder);
            SearchResponse response = client.search(esRequest, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits()) {
                CollegeDTO college = new CollegeDTO();
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                college.setId(sourceMap.getOrDefault("id", "").toString());
                college.setCollegeName(sourceMap.getOrDefault("collegeName", "").toString());
                college.setCollegeCode(sourceMap.getOrDefault("collegeCode", "").toString());
                college.setCollegeLocation(sourceMap.getOrDefault("collegeLocation", "").toString());
                college.setCollegeEmail(sourceMap.getOrDefault("collegeEmail", "").toString());
                college.setCollegePhone(sourceMap.getOrDefault("collegePhone", "").toString());
                college.setCollegeWebSiteLink(sourceMap.getOrDefault("collegeWebSiteLink", "").toString());
                college.setCollegeDescription(sourceMap.getOrDefault("collegeDescription", "").toString());
                Object coursesObj = sourceMap.get("courses");
                if (coursesObj instanceof List) {
                    List<CourseDTO> courseList = new ArrayList<>();
                    for (Object courseData : (List<?>) coursesObj) {
                        if (courseData instanceof Map) {
                            Map<?, ?> courseMap = (Map<?, ?>) courseData;
                            CourseDTO course = new CourseDTO();
                            course.setCourseName(courseMap.getOrDefault("courseName", null).toString());
                            courseList.add(course);
                        }
                    }
                    college.setCourses(courseList);
                } else {
                    college.setCourses(new ArrayList<>());
                }
                collegeList.add(college);
            }
            Aggregations aggregations = response.getAggregations();
            Terms locationTerms = aggregations.get("locationAgg");
            if (locationTerms != null) {
                locationTerms.getBuckets().forEach(bucket -> locations.add(bucket.getKeyAsString()));
            }
            Nested courseNested = aggregations.get("coursesAgg");
            if (courseNested != null) {
                Terms courseTerms = courseNested.getAggregations().get("courseNameAgg");
                if (courseTerms != null) {
                    courseTerms.getBuckets().forEach(bucket -> courses.add(bucket.getKeyAsString()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error during Elasticsearch query execution", e);
        }
        return new GenericResponse(collegeList, locations, courses);
    }
}
