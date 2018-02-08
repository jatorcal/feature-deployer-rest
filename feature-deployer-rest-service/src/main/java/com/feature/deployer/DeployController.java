package com.feature.deployer;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.feature.deployer.deploy.services.DeployService;

import feature.deployer.resources.deploy.DeployFrontResource;
import feature.deployer.resources.deploy.TomcatAPIResource;
import feature.deployer.resources.mysql.CurrentFrontDeployResource;

@RestController
public class DeployController {

	@Autowired
	private DeployService deployService;

    @RequestMapping(value = "/tomcat/deploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> tomcatDeploy(@RequestBody TomcatAPIResource tomcatAPIResources) 
    		throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	return deployService.tomcatDeploy(tomcatAPIResources);
    }
    
    @RequestMapping(value = "/tomcat/check/deploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PUT)
    public ResponseEntity<String> tomcatCheckDeploy(@RequestBody TomcatAPIResource tomcatAPIResources) 
    		throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	return deployService.tomcatCheckDeploy(tomcatAPIResources);
    }
    
    @RequestMapping(value = "/tomcat/undeploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> tomcatUndeploy(@RequestBody TomcatAPIResource tomcatAPIResources) 
    		throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	return deployService.tomcatUndeploy(tomcatAPIResources);
    }
    
    
    @RequestMapping(value = "/front/deploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> frontDeploy(@RequestBody DeployFrontResource deployFrontResource) 
    		throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	return deployService.frontDeploy(deployFrontResource);
    }
    
    @RequestMapping(value = "/front/check/deploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public CurrentFrontDeployResource frontCheckDeploy(@RequestBody DeployFrontResource deployFrontResource) 
    		throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	return deployService.checkDeploy(deployFrontResource);
    }
    
    
    @RequestMapping(value = "/front/undeploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> frontUndeploy(@RequestBody DeployFrontResource deployFrontResource) 
    		throws IOException, InterruptedException, URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    	return deployService.frontUndeploy(deployFrontResource);
    }
}