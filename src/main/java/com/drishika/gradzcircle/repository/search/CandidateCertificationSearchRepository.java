package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateCertification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateCertification entity.
 */
public interface CandidateCertificationSearchRepository extends ElasticsearchRepository<CandidateCertification, Long> {
}
