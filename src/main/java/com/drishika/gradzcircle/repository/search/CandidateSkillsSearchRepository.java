package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateSkills;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateSkills entity.
 */
public interface CandidateSkillsSearchRepository extends ElasticsearchRepository<CandidateSkills, Long> {
}
