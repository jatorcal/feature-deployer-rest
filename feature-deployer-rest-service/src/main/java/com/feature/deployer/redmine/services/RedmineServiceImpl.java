package com.feature.deployer.redmine.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import feature.deployer.resources.redmine.RedmineIssueResource;
import feature.deployer.resources.redmine.RedmineResource;

@Service("redmineService")
public class RedmineServiceImpl implements RedmineService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedmineServiceImpl.class);

	@Override
	public RedmineIssueResource issue(RedmineResource redmineResource) {
		
		// REST TEMPLATE
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getInterceptors().add(
				  new BasicAuthorizationInterceptor(redmineResource.getUser(), redmineResource.getPassword()));
		
		// Definimos los headers
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		
		// Creamos el request con el objeto
		HttpEntity requestEntity = new HttpEntity<>(headers);
		
		// Creamos el request con el objeto		
		ResponseEntity<RedmineIssueResource> response = restTemplate.exchange("http://redmine.lares.dsd/issues/46091.json", HttpMethod.GET, requestEntity, RedmineIssueResource.class);			
		
				
		return response.getBody();
	}
}
