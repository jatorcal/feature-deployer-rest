package com.feature.deployer.mvn.services;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import feature.deployer.resources.mvn.MVNResource;

public interface MVNService {
	
	/**
	 * Execute "mvn clean install" on path
	 * 
	 * @param {@link MVNResource} mvnResource
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ResponseEntity<String> build(MVNResource mvnResource) throws IOException, InterruptedException;
}
