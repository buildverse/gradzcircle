package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.EmploymentType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EmploymentType entity.
 */
public interface EmploymentTypeSearchRepository extends ElasticsearchRepository<EmploymentType, Long> {
}
