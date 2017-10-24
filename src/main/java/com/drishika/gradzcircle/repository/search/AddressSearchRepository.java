package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.Address;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Address entity.
 */
public interface AddressSearchRepository extends ElasticsearchRepository<Address, Long> {
}
