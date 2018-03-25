package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.JobFilterHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobFilterHistory entity.
 */
public interface JobFilterHistorySearchRepository extends ElasticsearchRepository<JobFilterHistory, Long> {
}
