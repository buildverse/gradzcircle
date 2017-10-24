package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CaptureCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CaptureCourse entity.
 */
public interface CaptureCourseSearchRepository extends ElasticsearchRepository<CaptureCourse, Long> {
}
