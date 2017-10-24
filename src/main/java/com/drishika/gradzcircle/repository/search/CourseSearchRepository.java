package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Course;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Course entity.
 */
public interface CourseSearchRepository extends ElasticsearchRepository<Course, Long> {
}
