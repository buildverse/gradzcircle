package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Audit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Audit entity.
 */
public interface AuditSearchRepository extends ElasticsearchRepository<Audit, Long> {
}
