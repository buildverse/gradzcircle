package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Language;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Language entity.
 */
public interface LanguageSearchRepository extends ElasticsearchRepository<Language, Long> {
}
