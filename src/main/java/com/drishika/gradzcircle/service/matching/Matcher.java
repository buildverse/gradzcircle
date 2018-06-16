
/**
 * Matcher Interface - Observer Interface
 */
package com.drishika.gradzcircle.service.matching;

import org.springframework.stereotype.Service;

/**
 * @author abhinav
 *
 */
@Service
public interface Matcher<T> {
	
	public void match (T t);

}
