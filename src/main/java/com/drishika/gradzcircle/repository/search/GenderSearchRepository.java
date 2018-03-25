package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.elastic.Gender;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Gender entity.
 */
public interface GenderSearchRepository extends ElasticsearchRepository<Gender, Long> {
}
