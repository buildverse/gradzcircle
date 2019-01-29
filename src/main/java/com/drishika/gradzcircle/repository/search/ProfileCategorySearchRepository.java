package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.ProfileCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfileCategory entity.
 */
public interface ProfileCategorySearchRepository extends ElasticsearchRepository<ProfileCategory, Long> {
}
