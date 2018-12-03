package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.States;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the States entity.
 */
public interface StatesSearchRepository extends ElasticsearchRepository<States, Long> {
}
