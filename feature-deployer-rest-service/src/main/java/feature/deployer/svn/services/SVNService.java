package feature.deployer.svn.services;

import org.tmatesoft.svn.core.SVNException;

import feature.deployer.resources.svn.SVNResource;

public interface SVNService {
	
	/**
	 * Checkout maven project from svn in a temporal path
	 * 
	 * 
	 * @param SVNJson svnJson
	 * 
	 * @throws SVNException
	 */
	public void checkout(SVNResource svnResource);
  
}
