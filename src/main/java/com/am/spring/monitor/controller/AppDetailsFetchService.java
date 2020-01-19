package com.am.spring.monitor.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AppDetailsFetchService {

	private final Logger LOGGER = LoggerFactory.getLogger(AppDetailsFetchService.class);

	public String fetchServiceState(String urlString) throws JsonMappingException, JsonProcessingException {
		JsonNode rootNode = CommonRestExecutor(urlString);
		LOGGER.info("Status " + rootNode.get("status").asText("NA"));
		return rootNode.get("status").asText("NA");
	}

	public String fetchStartTime(String urlString) throws JsonMappingException, JsonProcessingException {
		
		JsonNode rootNode = CommonRestExecutor(urlString);
		LOGGER.info("StartTime " + rootNode.get("measurements").get(0).get("value").asLong());
		Date date = new Date(rootNode.get("measurements").get(0).get("value").asLong()*1000);	
		return date.toString();
		
	}

	public long fetchUpTime(String urlString) throws JsonMappingException, JsonProcessingException {
		JsonNode rootNode = CommonRestExecutor(urlString);
		LOGGER.info("UpTime " + rootNode.get("measurements").get(0).get("value").asLong());
		return rootNode.get("measurements").get(0).get("value").asLong()/60;
	}

	public JsonNode CommonRestExecutor(String urlString) throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(urlString, String.class);
		LOGGER.info("Received for " + urlString + " : " + response.getBody());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readValue(response.getBody(), JsonNode.class);
		return rootNode;
	}
}
