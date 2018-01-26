package com.feature.deployer.deploy.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import feature.deployer.resources.deploy.TomcatAPIResources;

@Service("deployService")
public class DeployServiceImpl implements DeployService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployServiceImpl.class);

	private String TOMCAT_SERVICE_RESPONSE_FAIL = "FALLO";	


	/**
	 * Deploy war on tomcat
	 * 
	 * @param {@link TomcatAPIResources} tomcatAPIResources
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseEntity<String> tomcatDeploy(TomcatAPIResources tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {

		// REST TEMPLATE
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getInterceptors().add(
				  new BasicAuthorizationInterceptor(tomcatAPIResources.getUser(), tomcatAPIResources.getPassword()));
		
		// Definimos los headers
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		
		// Creamos el request con el objeto
		HttpEntity requestEntity = new HttpEntity<>(headers);
		
		// Creamos el request con el objeto		
		ResponseEntity<String> response = restTemplate.exchange(getURITomcatDeploy(tomcatAPIResources), HttpMethod.PUT, requestEntity, String.class);			
		if (response.getBody().trim().startsWith(TOMCAT_SERVICE_RESPONSE_FAIL)) {
			LOGGER.error("ERROR on tomcatDeploy action: {}", response);			
			throw new IOException(response.getBody());
		}
		
		return new ResponseEntity<>(HttpStatus.OK);		
	}
	
	/**
	 * Undeploy context on tomcat
	 * 
	 * @param {@link TomcatAPIResources} tomcatAPIResources
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	@Override
	public ResponseEntity<String> tomcatUndeploy(TomcatAPIResources tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {

		
		// REST TEMPLATE
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.getInterceptors().add(
				  new BasicAuthorizationInterceptor(tomcatAPIResources.getUser(), tomcatAPIResources.getPassword()));
		
		
		// Definimos los headers
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		
		// Creamos el request con el objeto
		String response = restTemplate.getForObject(getURITomcatUndeploy(tomcatAPIResources), String.class);
		
		if (response.trim().startsWith(TOMCAT_SERVICE_RESPONSE_FAIL)) {
			LOGGER.error("ERROR on tomcatUndeploy action: {}", response);			
			throw new IOException(response);
		}
		
		System.out.println(response);
		
		
		
			
		return new ResponseEntity<>(HttpStatus.OK);		
	}
	
	
	/**
	 * Returns uri for rest service tomcat deploy.
	 * This rest service has a PUT verb.
	 *  
	 * Example: http://user:user@localhost:8080/manager/text/deploy?path=/example&war=file:/home/example.war
	 * 
	 * @param {@link TomcatAPIResources}
	 * 
	 * @return String
	 * 
	 * @throws URISyntaxException
	 */
	private String getURITomcatDeploy(TomcatAPIResources tomcatAPIResources){
		String uri = new StringBuilder("http://")
			.append(tomcatAPIResources.getUser())
			.append(":")
			.append(tomcatAPIResources.getPassword())
			.append("@")
			.append(tomcatAPIResources.getHost())
			.append(":")
			.append(tomcatAPIResources.getPort())
			.append("/manager/text/deploy?path=")
			.append(tomcatAPIResources.getContext())
			.append("&war=file>")
			.append(tomcatAPIResources.getWarPath()).toString();
		
		return uri;
	}
	
	
	/**
	 * Returns uri for rest service tomcat deploy.
	 * This rest service has a PUT verb.
	 *  
	 * Example: http://user:user@localhost:8080/manager/text/undeploy?path=/example
	 * 
	 * @param {@link TomcatAPIResources}
	 * 
	 * @return String
	 * 
	 * @throws URISyntaxException
	 */
	private String getURITomcatUndeploy(TomcatAPIResources tomcatAPIResources) throws URISyntaxException {
		String uri = new StringBuilder("http://")
			.append(tomcatAPIResources.getUser())
			.append(":")
			.append(tomcatAPIResources.getPassword())
			.append("@")
			.append(tomcatAPIResources.getHost())
			.append(":")
			.append(tomcatAPIResources.getPort())
			.append("/manager/text/undeploy?path=")
			.append(tomcatAPIResources.getContext()).toString();
		
		return uri;
	}
	
}
