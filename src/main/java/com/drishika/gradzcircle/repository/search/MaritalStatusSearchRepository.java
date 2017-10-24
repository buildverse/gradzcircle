package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.MaritalStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MaritalStatus entity.
 */
public interface MaritalStatusSearchRepository extends ElasticsearchRepository<MaritalStatus, Long> {
}
