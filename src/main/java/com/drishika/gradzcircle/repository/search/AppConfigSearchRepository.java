package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.AppConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AppConfig entity.
 */
public interface AppConfigSearchRepository extends ElasticsearchRepository<AppConfig, Long> {
}
