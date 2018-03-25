package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.elastic.College;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the College entity.
 */
public interface CollegeSearchRepository extends ElasticsearchRepository<College, Long> {
}
