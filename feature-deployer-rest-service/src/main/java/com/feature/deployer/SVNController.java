package com.feature.deployer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tmatesoft.svn.core.SVNException;

import com.feature.deployer.svn.services.SVNService;

import feature.deployer.resources.svn.SVNResource;

@RestController
public class SVNController {

	@Autowired
	private SVNService svnService;

    @RequestMapping(value = "/checkout", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public void checkout(@RequestBody SVNResource svnResource) {
    	svnService.checkout(svnResource);
    }
    
    
    @RequestMapping(value = "/showHistory", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PUT)
    public void checkHistory(@RequestBody SVNResource svnResource) {
    	
    	try {
			svnService.showHistory(svnResource);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
}