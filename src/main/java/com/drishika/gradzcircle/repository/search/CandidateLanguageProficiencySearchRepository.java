package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateLanguageProficiency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateLanguageProficiency entity.
 */
public interface CandidateLanguageProficiencySearchRepository extends ElasticsearchRepository<CandidateLanguageProficiency, Long> {
}
