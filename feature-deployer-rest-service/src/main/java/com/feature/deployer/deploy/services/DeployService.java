package com.feature.deployer.deploy.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;

import feature.deployer.resources.deploy.DeployFrontResource;
import feature.deployer.resources.deploy.TomcatAPIResource;
import feature.deployer.resources.mysql.CurrentFrontDeployResource;

public interface DeployService {

	/**
	 * Deploy war on tomcat
	 * 
	 * @param {@link TomcatAPIResource} tomcatAPIResources
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	public ResponseEntity<String> tomcatDeploy(TomcatAPIResource tomcatAPIResources)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

	
	
	/**
	 * Check if context is deploy on tomcat yet
	 * 
	 * @param {@link TomcatAPIResource} tomcatAPIResources
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	public ResponseEntity<String> tomcatCheckDeploy(TomcatAPIResource tomcatAPIResources)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	/**
	 * Undeploy context on tomcat
	 * 
	 * @param {@link TomcatAPIResource} tomcatAPIResources
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	public ResponseEntity<String> tomcatUndeploy(TomcatAPIResource tomcatAPIResources)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	/**
	 * Deploy front
	 * 
	 * @param {@link DeployFrontResource} deployFrontResource
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	public ResponseEntity<String> frontDeploy(DeployFrontResource deployFrontResource)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	/**
	 * Undeploy front
	 * 
	 * @param {@link DeployFrontResource} deployFrontResource
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	public ResponseEntity<String> frontUndeploy(DeployFrontResource deployFrontResource)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	/**
	 * Check existing deploy front
	 * 
	 * @param {@link DeployFrontResource} deployFrontResource
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	public CurrentFrontDeployResource checkDeploy(DeployFrontResource deployFrontResource)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
}
