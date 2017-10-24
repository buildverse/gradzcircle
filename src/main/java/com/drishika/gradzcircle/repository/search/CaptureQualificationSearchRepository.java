package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CaptureQualification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CaptureQualification entity.
 */
public interface CaptureQualificationSearchRepository extends ElasticsearchRepository<CaptureQualification, Long> {
}
