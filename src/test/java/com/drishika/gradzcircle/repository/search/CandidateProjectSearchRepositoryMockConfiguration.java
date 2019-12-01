/**
 * 
 */
package com.drishika.gradzcircle.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author abhinav
 *
 */
@Configuration
public class CandidateProjectSearchRepositoryMockConfiguration {
	
	@MockBean
	private CandidateProjectSearchRepository mockCandidateProjectSearchRepository;


}
