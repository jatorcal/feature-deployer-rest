package com.feature.deployer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.feature.deployer.mvn.services.MVNService;

import feature.deployer.resources.mvn.MVNResource;

@RestController
public class MVNController {

	@Autowired
	private MVNService mvnService;

    @RequestMapping(value = "/build", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<String> build(@RequestBody MVNResource mvnResource) throws IOException, InterruptedException {
    	return mvnService.build(mvnResource);
    }
}