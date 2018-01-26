package com.feature.deployer.deploy.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;

import feature.deployer.resources.deploy.TomcatAPIResources;

public interface DeployService {

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
	public ResponseEntity<String> tomcatDeploy(TomcatAPIResources tomcatAPIResources)
			throws IOException, InterruptedException, URISyntaxException;

	
	
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
	public ResponseEntity<String> tomcatUndeploy(TomcatAPIResources tomcatAPIResources)
			throws IOException, InterruptedException, URISyntaxException;
}
