package com.drishika.gradzcircle.repository.search;

import com.drishika.gradzcircle.domain.CandidateEducation;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CandidateEducation entity.
 */
public interface CandidateEducationSearchRepository extends ElasticsearchRepository<CandidateEducation, Long> {

    List<CandidateEducation> findByCandidateIdOrderByEducationToDateDesc(String id);

}
