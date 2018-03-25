package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.JobFilter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobFilter entity.
 */
public interface JobFilterSearchRepository extends ElasticsearchRepository<JobFilter, Long> {
}
