package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.VisaType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the VisaType entity.
 */
public interface VisaTypeSearchRepository extends ElasticsearchRepository<VisaType, Long> {
}
