package com.drishika.gradzcircle.web.rest.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestSerilaze {

	public static void main(String[] args) throws JsonProcessingException, IOException {
		// TODO Auto-generated method stub
		
		String json = "{\"name\":\"abhinav\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNodeRoot = mapper.readTree(json.getBytes());
		System.out.println(mapper.writeValueAsString(json));
	}

}
