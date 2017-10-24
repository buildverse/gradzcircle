package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateEmployment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateEmployment entity.
 */
public interface CandidateEmploymentSearchRepository extends ElasticsearchRepository<CandidateEmployment, Long> {
}
