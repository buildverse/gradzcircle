package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.University;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the University entity.
 */
public interface UniversitySearchRepository extends ElasticsearchRepository<University, Long> {
}
