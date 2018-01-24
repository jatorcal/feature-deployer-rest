package feature.deployer.svn.services;

import java.io.IOException;

import feature.deployer.resources.mvn.MVNResource;

public interface MVNService {
	
	/**
	 * Execute "mvn clean install" on path
	 * 
	 * @param path
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean build(MVNResource mvnResource) throws IOException, InterruptedException;
}
