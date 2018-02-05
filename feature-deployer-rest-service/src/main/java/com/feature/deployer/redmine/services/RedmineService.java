package com.feature.deployer.redmine.services;

import feature.deployer.resources.redmine.RedmineIssueResource;
import feature.deployer.resources.redmine.RedmineResource;

public interface RedmineService {

	
	public RedmineIssueResource issue(RedmineResource redmineResource);
  
}
