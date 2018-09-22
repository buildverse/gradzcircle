/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.util.Set;

import org.springframework.context.ApplicationEvent;

import com.drishika.gradzcircle.web.websocket.dto.MatchActivityDTO;

/**
 * @author abhinav
 *
 */
public class MatchEvent extends ApplicationEvent {
	
	private final Set<MatchActivityDTO> matchActivityDTO;
	
	public MatchEvent(Object source, Set<MatchActivityDTO> matchActivityDTO) {
        super(source);
        this.matchActivityDTO = matchActivityDTO;
    }

    public Set<MatchActivityDTO> getMatchingActivityDTO() {
        return matchActivityDTO;
    }

}
