package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Skills;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Skills entity.
 */
public interface SkillsSearchRepository extends ElasticsearchRepository<Skills, Long> {
}
