package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Qualification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Qualification entity.
 */
public interface QualificationSearchRepository extends ElasticsearchRepository<Qualification, Long> {
}
