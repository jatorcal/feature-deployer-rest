package feature.deployer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import feature.deployer.resources.mvn.MVNResource;
import feature.deployer.svn.services.MVNService;

@RestController
public class MVNController {

	@Autowired
	private MVNService mvnService;

    @RequestMapping(value = "/build", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public void build(@RequestBody MVNResource mvnResource) throws IOException, InterruptedException {
    	mvnService.build(mvnResource);
    }
}