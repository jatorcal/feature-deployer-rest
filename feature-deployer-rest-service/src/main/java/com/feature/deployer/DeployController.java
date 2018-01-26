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

import feature.deployer.resources.deploy.TomcatAPIResources;

@RestController
public class DeployController {

	@Autowired
	private DeployService deployService;

    @RequestMapping(value = "/tomcat/deploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> build(@RequestBody TomcatAPIResources tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {
    	return deployService.tomcatDeploy(tomcatAPIResources);
    }
    
    @RequestMapping(value = "/tomcat/undeploy", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> undeploy(@RequestBody TomcatAPIResources tomcatAPIResources) throws IOException, InterruptedException, URISyntaxException {
    	return deployService.tomcatUndeploy(tomcatAPIResources);
    }
    
}