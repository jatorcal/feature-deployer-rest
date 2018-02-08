package com.feature.persistence.services;

import java.util.List;

import feature.deployer.resources.mysql.CurrentFrontDeployResource;

public interface CurrentFrontDeployProcessDAO {
	
	public List<CurrentFrontDeployResource> all();

	public CurrentFrontDeployResource currentprojectName(String projectName);

	public void insert(CurrentFrontDeployResource currentFrontDeployResource);

	public void delete(CurrentFrontDeployResource currentFrontDeployResource);
	
	public void delete(String projectName);
	
	

}
