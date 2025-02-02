package com.edusphere.portal.ElasticSearchConfiguration;


import com.edusphere.portal.dto.CollegeDTO;
import com.edusphere.portal.dto.CourseDTO;
import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.dto.SearchDTO;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.global.ParsedGlobal;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ElasticSearchClient {

    private final RestHighLevelClient client;

    @Autowired
    public ElasticSearchClient(RestHighLevelClient client) {
        this.client = client;
    }

    public GenericResponse searchColleges(SearchDTO searchRequest) {
        List<CollegeDTO> collegeList = new ArrayList<>();
        List<String> locations = new ArrayList<>();
        List<String> courses = new ArrayList<>();

        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (searchRequest.getQ() != null && !searchRequest.getQ().isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("collegeName", searchRequest.getQ()));
            } else {
                sourceBuilder.query(QueryBuilders.matchAllQuery());
            }

            if (searchRequest.getFilter() != null && !searchRequest.getFilter().isEmpty()) {
                searchRequest.getFilter().forEach(filter -> {
                    String field = filter.getField();
                    String value = filter.getValue();

                    if (value == null || value.isEmpty()) {
                        if ("courses.courseName".equals(field)) {
                            boolQuery.filter(QueryBuilders.matchQuery("courses.courseName", ""));
                        } else if ("collegeLocation".equals(field)) {
                            boolQuery.filter(QueryBuilders.matchQuery("collegeLocation", ""));
                        }
                    } else {
                        if ("courses.courseName".equals(field)) {
                            boolQuery.filter(QueryBuilders.termQuery("courses.courseName", value));
                        } else {
                            boolQuery.filter(QueryBuilders.termQuery(field, value));
                        }
                    }
                });
            }

            // Set the query for the source builder
            if (boolQuery.hasClauses()) {
                sourceBuilder.query(boolQuery);
            }

            // Aggregation for location and course names
            sourceBuilder.aggregation(
                    AggregationBuilders.global("global_agg")
                            .subAggregation(AggregationBuilders.terms("locationAgg").field("collegeLocation"))
                            .subAggregation(AggregationBuilders.terms("coursesAgg")
                                    .field("courses.courseName"))
            );

            // Search request
            SearchRequest esRequest = new SearchRequest("colleges");
            esRequest.source(sourceBuilder);
            SearchResponse response = client.search(esRequest, RequestOptions.DEFAULT);

            // Extracting the search results
            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> sourceMap = hit.getSourceAsMap();
                CollegeDTO college = mapCollegeDTO(sourceMap);
                collegeList.add(college);
            }

            // Extracting aggregations for locations and courses
            Aggregations aggregations = response.getAggregations();
            if (aggregations != null) {
                ParsedGlobal globalAgg = aggregations.get("global_agg");
                if (globalAgg != null) {
                    Terms locationTerms = globalAgg.getAggregations().get("locationAgg");
                    if (locationTerms != null) {
                        locations.addAll(locationTerms.getBuckets().stream()
                                .map(Terms.Bucket::getKeyAsString)
                                .collect(Collectors.toList()));
                    }

                    Terms courseTerms = globalAgg.getAggregations().get("coursesAgg");
                    if (courseTerms != null) {
                        courses.addAll(courseTerms.getBuckets().stream()
                                .map(Terms.Bucket::getKeyAsString)
                                .collect(Collectors.toList()));
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error during Elasticsearch query execution", e);
        }

        return new GenericResponse(collegeList, locations, courses);
    }


    private CollegeDTO mapCollegeDTO(Map<String, Object> sourceMap) {
        CollegeDTO college = new CollegeDTO();
        college.setId(String.valueOf(sourceMap.getOrDefault("id", "")));
        college.setCollegeName(String.valueOf(sourceMap.getOrDefault("collegeName", "")));
        college.setCollegeCode(String.valueOf(sourceMap.getOrDefault("collegeCode", "")));
        college.setCollegeLocation(String.valueOf(sourceMap.getOrDefault("collegeLocation", "")));
        college.setCollegeEmail(String.valueOf(sourceMap.getOrDefault("collegeEmail", "")));
        college.setCollegePhone(String.valueOf(sourceMap.getOrDefault("collegePhone", "")));
        college.setCollegeWebSiteLink(String.valueOf(sourceMap.getOrDefault("collegeWebSiteLink", "")));
        college.setCollegeDescription(String.valueOf(sourceMap.getOrDefault("collegeDescription", "")));

        Object coursesObj = sourceMap.get("courses");
        if (coursesObj instanceof List<?>) {
            List<CourseDTO> courseList = ((List<?>) coursesObj).stream()
                    .filter(Map.class::isInstance)
                    .map(courseData -> {
                        Map<?, ?> courseMap = (Map<?, ?>) courseData;
                        CourseDTO course = new CourseDTO();
                        course.setCourseName(String.valueOf(courseMap.getOrDefault("courseName", null)));
                        return course;
                    })
                    .toList();
            college.setCourses(courseList);
        } else {
            college.setCourses(new ArrayList<>());
        }

        return college;
    }
}
