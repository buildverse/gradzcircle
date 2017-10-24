package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.JobCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the JobCategory entity.
 */
public interface JobCategorySearchRepository extends ElasticsearchRepository<JobCategory, Long> {
}
