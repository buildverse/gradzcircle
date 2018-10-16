package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateNonAcademicWork;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateNonAcademicWork entity.
 */
public interface CandidateNonAcademicWorkSearchRepository
		extends ElasticsearchRepository<CandidateNonAcademicWork, Long> {
}
