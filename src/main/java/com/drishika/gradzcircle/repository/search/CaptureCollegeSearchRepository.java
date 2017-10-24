package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CaptureCollege;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CaptureCollege entity.
 */
public interface CaptureCollegeSearchRepository extends ElasticsearchRepository<CaptureCollege, Long> {
}
