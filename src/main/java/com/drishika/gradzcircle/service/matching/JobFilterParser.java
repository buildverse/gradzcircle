/**
 * 
 */
package com.drishika.gradzcircle.service.matching;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author abhinav
 *
 */
@Service
public class JobFilterParser {

	private static final Logger logger = LoggerFactory.getLogger(JobFilterParser.class);

	ObjectMapper mapper;

	public JobFilterParser() {
		mapper = new ObjectMapper();
	}

	public JobFilterObject getFilterObjectFromJson(String filterDescription) {
		JobFilterObject filterObject = null;
		try {
			filterObject = mapper.readValue(filterDescription, JobFilterObject.class);
		} catch (JsonParseException e) {
			logger.error("Error creating JobfilterObject {}", e.fillInStackTrace());
		} catch (JsonMappingException e) {
			logger.error("Error creating JobfilterObject {}", e.fillInStackTrace());
		} catch (IOException e) {
			logger.error("Error creating JobfilterObject {}", e.fillInStackTrace());
		}
		return filterObject;
	}

	public String getFilterDescriptionAsString(JobFilterObject filterObject) {
		return null;
	}

}
