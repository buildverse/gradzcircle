package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Filter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Filter entity.
 */
public interface FilterSearchRepository extends ElasticsearchRepository<Filter, Long> {
}
