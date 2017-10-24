package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateProject entity.
 */
public interface CandidateProjectSearchRepository extends ElasticsearchRepository<CandidateProject, Long> {
}
