package com.feature.deployer.npm.services;

import java.io.IOException;
import java.util.List;

import feature.deployer.resources.deploy.DeployFrontResource;
import feature.deployer.resources.deploy.ServerJsParameterResource;

public interface NPMService {
	
	
	/**
	 * Edit server.js with declared parameters by user.
	 * 
	 * Will edit vars with a new value.
	 * 
	 * 
	 * @author jta010es
	 * 
	 * @param String projectsSourceCodePath
	 * 
	 * @param List<ServerJsParameterResource> serverJsParameters 
	 * 
	 */
	public void editServerJs(String projectsSourceCodePath, List<ServerJsParameterResource> serverJsParameters) 
			throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	
	/**
	 * Execute "npm install" on path
	 * 
	 * @param {@link String} projectsSourceCodePath
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void npmInstall(String projectsSourceCodePath) 
			throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	/**
	 * Execute "gulp build" on path
	 * 
	 * @param {@link String} projectsSourceCodePath
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void gulpBuild(String projectsSourceCodePath) 
			throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
	/**
	 * Execute "npm start dev" on path
	 * 
	 * @param {@link String} projectsSourceCodePath
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void npmStartDev(DeployFrontResource deployFrontResource) 
			throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
}
