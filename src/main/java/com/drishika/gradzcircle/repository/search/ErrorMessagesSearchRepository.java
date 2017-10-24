package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.ErrorMessages;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ErrorMessages entity.
 */
public interface ErrorMessagesSearchRepository extends ElasticsearchRepository<ErrorMessages, Long> {
}
