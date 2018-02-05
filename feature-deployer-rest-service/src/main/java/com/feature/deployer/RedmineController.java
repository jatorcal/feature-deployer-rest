package com.feature.deployer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feature.deployer.redmine.services.RedmineService;

import feature.deployer.resources.redmine.RedmineResource;

@RestController
public class RedmineController {
	
	@Autowired
	private RedmineService redmineService;

    @RequestMapping("/comparer")
    public void comparer(@RequestBody RedmineResource redmineResource) {
    	
    	redmineService.issue(redmineResource);
    }
}