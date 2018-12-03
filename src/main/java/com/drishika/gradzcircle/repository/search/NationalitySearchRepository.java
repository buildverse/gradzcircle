package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.elastic.Nationality;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Nationality entity.
 */
public interface NationalitySearchRepository extends ElasticsearchRepository<Nationality, Long> {
}
