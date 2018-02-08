package com.feature.persistence.services;

import java.util.List;

import feature.deployer.resources.mysql.BashProcessResource;

public interface BashProcessDAO {

	List<BashProcessResource> all();

	List<BashProcessResource> id(Integer pid);

	void insert(BashProcessResource bashProcessResource);

	void delete(Integer pid);
	
	

}
