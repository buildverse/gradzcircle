/**
 * 
 */
package com.drishika.gradzcircle.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author abhinav
 *
 */
@Configuration
public class AppConfigSearchRepositoryMockConfiguration {
	@MockBean
	private AppConfigSearchRepository mockAppConfigSearchRepository;

}
