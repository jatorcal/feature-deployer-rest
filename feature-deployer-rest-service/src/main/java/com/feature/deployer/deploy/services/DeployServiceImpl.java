package com.feature.deployer.deploy.services;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.feature.deployer.command.services.CommandService;
import com.feature.deployer.npm.services.NPMService;
import com.feature.persistence.services.BashProcessDAO;
import com.feature.persistence.services.BashProcessDAOImpl;
import com.feature.persistence.services.CurrentFrontDeployProcessDAO;
import com.feature.persistence.services.CurrentFrontDeployProcessDAOImpl;

import feature.deployer.resources.deploy.DeployFrontResource;
import feature.deployer.resources.deploy.TomcatAPIResource;
import feature.deployer.resources.mysql.BashProcessResource;
import feature.deployer.resources.mysql.CurrentFrontDeployResource;

@Service("deployService")
public class DeployServiceImpl implements DeployService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployServiceImpl.class);

	private String TOMCAT_SERVICE_RESPONSE_FAIL = "FALLO";	
	
	@Autowired
	private NPMService npmService;
	
	@Autowired
	private CommandService commandService;


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
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseEntity<String> tomcatDeploy(TomcatAPIResource tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {

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
	 * Check if context is deploy on tomcat yet
	 * 
	 * @param {@link TomcatAPIResource} tomcatAPIResources
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseEntity<String> tomcatCheckDeploy(TomcatAPIResource tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {

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
		ResponseEntity<String> response = restTemplate.exchange(getURITomcatCheckDeploy(tomcatAPIResources), HttpMethod.GET, requestEntity, String.class);
		
		String listAppDeploy = response.getBody();
		StringTokenizer tokenizer = new StringTokenizer(listAppDeploy, "\n");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();			
			StringTokenizer tokenizer2 = new StringTokenizer(token, ":");
			int counter = 1;
			while (tokenizer2.hasMoreTokens()) {
				String token2 = tokenizer2.nextToken();
				if (counter==4) {
					if (token2.equals(tomcatAPIResources.getContext())) {
						throw new IOException ("war.deploy.exists");
					}
				}
				counter++;
			}
			
		}
		
		return new ResponseEntity<>(HttpStatus.OK);		
	}
	
	
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
	@Override
	public ResponseEntity<String> tomcatUndeploy(TomcatAPIResource tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {

		
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
	 * Deploy front
	 * 
	 * @param {@link DeployFrontResource} deployFrontResource
	 * 
	 * @return ResponseEntity<String>
	 * 
	 * @throws IOException - Returns response body in case of error
	 * @throws InterruptedException, URISyntaxException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public ResponseEntity<String> frontDeploy(DeployFrontResource deployFrontResource)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		// editamos server.js
		npmService.editServerJs(deployFrontResource.getProjectsSourceCodePath(), deployFrontResource.getServerJsParameters());
		
		/** BUILD **/
		// npm install
		npmService.npmInstall(deployFrontResource.getProjectsSourceCodePath());
		
		// gulp build
		npmService.gulpBuild(deployFrontResource.getProjectsSourceCodePath());
		
		/** DEPLOY **/
		// npm start dev
		npmService.npmStartDev(deployFrontResource);
		
		return null;
	}
	
	
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
	@Override
	public ResponseEntity<String> frontUndeploy(DeployFrontResource deployFrontResource)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		
		
		CurrentFrontDeployResource currentFrontDeployResource = checkDeploy(deployFrontResource);
		
		
		BashProcessDAO bashProcessDAO = new BashProcessDAOImpl();
		List<BashProcessResource> listBashProcessResource = bashProcessDAO.id(currentFrontDeployResource.getPidBashProcess());
		
		for (BashProcessResource bashProcessResource : listBashProcessResource) {
			
			String cmdLinePid = new StringBuilder()
					.append("kill -9 ")
					.append(bashProcessResource.getPid())
					.toString();
			
			commandService.bufferedReaderLinesForCommandLine(cmdLinePid);
			
			
			
			String cmdLinePidAssociated = new StringBuilder()
					.append("kill -9 ")
					.append(bashProcessResource.getAssociatedPid())
					.toString();
			
			commandService.bufferedReaderLinesForCommandLine(cmdLinePidAssociated);
			
		}
		
		CurrentFrontDeployProcessDAO currentFrontDeployProcessDAO = new CurrentFrontDeployProcessDAOImpl();
		currentFrontDeployProcessDAO.delete(deployFrontResource.getFrontProject().getProjectName());
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	
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
	@Override
	public CurrentFrontDeployResource checkDeploy(DeployFrontResource deployFrontResource)
			throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		CurrentFrontDeployProcessDAO currentFrontDeployProcessDAO = new CurrentFrontDeployProcessDAOImpl();
		CurrentFrontDeployResource currentFrontDeployResource = currentFrontDeployProcessDAO.currentprojectName(deployFrontResource.getFrontProject().getProjectName());		
		return currentFrontDeployResource;
	}
	
	
	/**
	 * Returns uri for rest service tomcat deploy.
	 * This rest service has a PUT verb.
	 *  
	 * Example: http://user:user@localhost:8080/manager/text/deploy?path=/example&war=file:/home/example.war
	 * 
	 * @param {@link TomcatAPIResource}
	 * 
	 * @return String
	 * 
	 * @throws URISyntaxException
	 */
	private String getURITomcatDeploy(TomcatAPIResource tomcatAPIResources){
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
			.append("&war=file:")
			.append(tomcatAPIResources.getWarPath()).toString();
		
		return uri;
	}
	
	/**
	 * Returns uri for rest service tomcat list.
	 * This rest service has a GET verb.
	 *  
	 * Example: http://user:user@http://localhost:8081/manager/text/list
	 * 
	 * @param {@link TomcatAPIResource}
	 * 
	 * @return String
	 * 
	 * @throws URISyntaxException
	 */
	private String getURITomcatCheckDeploy(TomcatAPIResource tomcatAPIResources){
		String uri = new StringBuilder("http://")
			.append(tomcatAPIResources.getUser())
			.append(":")
			.append(tomcatAPIResources.getPassword())
			.append("@")
			.append(tomcatAPIResources.getHost())
			.append(":")
			.append(tomcatAPIResources.getPort())
			.append("/manager/text/list").toString();
		
		return uri;
	}
	
	
	/**
	 * Returns uri for rest service tomcat deploy.
	 * This rest service has a PUT verb.
	 *  
	 * Example: http://user:user@localhost:8080/manager/text/undeploy?path=/example
	 * 
	 * @param {@link TomcatAPIResource}
	 * 
	 * @return String
	 * 
	 * @throws URISyntaxException
	 */
	private String getURITomcatUndeploy(TomcatAPIResource tomcatAPIResources) throws URISyntaxException {
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
