
/**
 * Matcher Interface - Observer Interface
 */
package com.drishika.gradzcircle.service.matching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author abhinav
 *
 */
@Service
public interface Matcher<T> {
	@Async
	@Transactional
	public void match(T t);

}
