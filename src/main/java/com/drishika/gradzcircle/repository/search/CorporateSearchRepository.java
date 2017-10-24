package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Corporate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Corporate entity.
 */
public interface CorporateSearchRepository extends ElasticsearchRepository<Corporate, Long> {
}
