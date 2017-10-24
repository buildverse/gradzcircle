package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CaptureUniversity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CaptureUniversity entity.
 */
public interface CaptureUniversitySearchRepository extends ElasticsearchRepository<CaptureUniversity, Long> {
}
