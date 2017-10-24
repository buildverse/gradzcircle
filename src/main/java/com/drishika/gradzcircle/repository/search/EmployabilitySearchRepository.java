package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Employability;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Employability entity.
 */
public interface EmployabilitySearchRepository extends ElasticsearchRepository<Employability, Long> {
}
