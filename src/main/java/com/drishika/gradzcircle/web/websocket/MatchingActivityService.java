/**
 * 
 */
package com.drishika.gradzcircle.web.websocket;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.drishika.gradzcircle.domain.CandidateJob;
import com.drishika.gradzcircle.service.matching.MatchEvent;
import com.drishika.gradzcircle.web.websocket.dto.MatchActivityDTO;

/**
 * @author abhinav
 *
 */
@Controller
public class MatchingActivityService implements ApplicationListener<MatchEvent> {
	private static final Logger log = LoggerFactory.getLogger(MatchingActivityService.class);

	private final SimpMessageSendingOperations messagingTemplate;

	public MatchingActivityService(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@SubscribeMapping("/topic/matchActivity")
	@SendTo("/topic/match")
	public void sendActivity(@Payload MatchActivityDTO matchActivityDTO, StompHeaderAccessor stompHeaderAccessor,
			CandidateJob candidateJob) {
		log.debug(">>>>>>>>>>>>>>>>Client sending data works<<<<<<<<<<<<<<<<<");
		return;
		// log.debug("Sending matched data {}", matchActivityDTO);

	}

	@Override
	public void onApplicationEvent(MatchEvent event) {
		Set<MatchActivityDTO> dtos = event.getMatchingActivityDTO();
		log.debug("Am rececing match event to publish data {}", dtos);
		messagingTemplate.convertAndSend("/topic/match", dtos);
	}

}
