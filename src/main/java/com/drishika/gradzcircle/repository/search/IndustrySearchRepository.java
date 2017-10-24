package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Industry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Industry entity.
 */
public interface IndustrySearchRepository extends ElasticsearchRepository<Industry, Long> {
}
