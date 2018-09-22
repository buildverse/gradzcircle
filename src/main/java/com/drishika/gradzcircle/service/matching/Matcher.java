
/**
 * Matcher Interface - Observer Interface
 */
package com.drishika.gradzcircle.service.matching;

import java.util.Set;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drishika.gradzcircle.web.websocket.dto.MatchActivityDTO;

/**
 * @author abhinav
 *
 */
@Service
public interface Matcher<T> {
	@Async
	@Transactional
	public void match (T t);
	

}
