package feature.deployer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import feature.deployer.resources.svn.SVNResource;
import feature.deployer.svn.services.SVNService;

@RestController
public class SVNController {

	@Autowired
	private SVNService svnService;

    @RequestMapping(value = "/checkout", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public void checkout(@RequestParam SVNResource svnResource) {
    	svnService.checkout(svnResource);
    }
}