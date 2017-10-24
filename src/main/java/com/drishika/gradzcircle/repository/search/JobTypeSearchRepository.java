package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.JobType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobType entity.
 */
public interface JobTypeSearchRepository extends ElasticsearchRepository<JobType, Long> {
}
