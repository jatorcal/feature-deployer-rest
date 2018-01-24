package feature.deployer.svn.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import feature.deployer.resources.svn.SVNResource;

@Service("svnService")
public class SVNServiceImpl implements SVNService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SVNServiceImpl.class);

	public void checkout(SVNResource svnResource) {

		//RepositoryResource repositoryResource = projectResource.getRepositoryResource();
		SvnOperationFactory svnOperationFactory = new SvnOperationFactory();		
		svnOperationFactory.setAuthenticationManager(getAuthManager(svnResource));
		
		try {
			File workingCopy = new File(svnResource.getTempPath());
			/*if ((workingCopy.getAbsoluteFile().exists()) && (workingCopy.getAbsoluteFile().listFiles() != null)
					&& (workingCopy.getAbsoluteFile().listFiles().length != 0)) {
				SvnUpdate svnUpdate = svnOperationFactory.createUpdate();

				svnUpdate.setSingleTarget(SvnTarget.fromFile(workingCopy));
				svnUpdate.run();
			} else {*/
				workingCopy.mkdirs();

				SvnCheckout checkout = svnOperationFactory.createCheckout();
				checkout.setSingleTarget(SvnTarget.fromFile(workingCopy));
				checkout.setSource(SvnTarget.fromURL(SVNURL.parseURIDecoded(svnResource.getSvnPath())));
				checkout.run();
			//}
		} catch (SVNException e) {
			svnOperationFactory.dispose();
			LOGGER.error("ERROR connecting to repository: {}", e.getMessage());
			//throw new SVNException(e.getErrorMessage());
		}
	}

	
	/**
	 * Creates authenticated connection with subversion
	 * 
	 * @param repositoryResource
	 * @return ISVNAuthenticationManager
	 */
	private ISVNAuthenticationManager getAuthManager(SVNResource svnResource) {
		return new BasicAuthenticationManager(svnResource.getUser(), svnResource.getPassword());
	}

	
	/*private SVNRepository getSVNRepositoryLogged(RepositoryResource repositoryResource) throws SVNException {
		
		ISVNAuthenticationManager authManager = new BasicAuthenticationManager(repositoryResource.getUser(),
				repositoryResource.getPassword());

		SVNURL url = SVNURL.parseURIEncoded(repositoryResource.getValue());
		SVNRepository svnRepository = SVNRepositoryFactory.create(url, null);

		svnRepository.setAuthenticationManager(authManager);

		return svnRepository;
	}*/
}
