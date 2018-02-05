package com.feature.deployer.svn.services;

import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class RedmineSVN {

	public void main() {
		
		
	    DAVRepositoryFactory.setup( );

	    String url = "[REPOSITORY URL]";
	    String name = "anonymous";
	    String password = "anonymous";
	    long startRevision = 1000000;
	    long endRevision = 1000002; //HEAD (the latest) revision

	    SVNRepository repository = null;
	    try {
	        repository = SVNRepositoryFactory.create( SVNURL.parseURIEncoded( url ) );
	        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( name, password );
	        repository.setAuthenticationManager( authManager );


	        Collection logEntries = null;

	        logEntries = repository.log( new String[] { "" } , null , startRevision , endRevision , true , true );

	        for ( Iterator entries = logEntries.iterator( ); entries.hasNext( ); ) {
	            SVNLogEntry logEntry = ( SVNLogEntry ) entries.next( );
	            System.out.println (String.format("revision: %d, date %s", logEntry.getRevision( ), logEntry.getDate()));
	        }
	    } catch (Exception e){

	    }

	}

}
