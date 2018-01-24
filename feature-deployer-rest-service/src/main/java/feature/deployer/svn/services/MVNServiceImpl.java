package feature.deployer.svn.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import feature.deployer.resources.mvn.MVNResource;

@Service("mvnService")
public class MVNServiceImpl implements MVNService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MVNServiceImpl.class);
	
	private static String BUILD_SUCCESS = "BUILD SUCCESS";
	private static String BUILD_FAILURE = "BUILD FAILURE";

	public boolean build(MVNResource mvnResource) throws IOException, InterruptedException {
		return mvnCleanInstallCommand(mvnResource.getMvnProjectPath());
	}

	private boolean mvnCleanInstallCommand(String repoLocalPath) throws IOException, InterruptedException {
		String cmdline = "cd " + repoLocalPath + " && mvn clean install";
		boolean success = false;

		LOGGER.info("Execute command: {}", cmdline);

		Process process = new ProcessBuilder(new String[] { "bash", "-c", cmdline }).redirectErrorStream(true)
				.directory(new File(".")).start();

		ArrayList<String> output = new ArrayList();

		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = null;
		while ((line = br.readLine()) != null) {
			LOGGER.debug(line);
			output.add(line);
			if (line.contains(BUILD_SUCCESS)) {
				success = true;
			} else if (line.contains(BUILD_FAILURE)) {
				success = false;
			}
		}
		LOGGER.info("Finish command with result {}", Boolean.valueOf(success));
		if (0 != process.waitFor()) {
			return false;
		}
		if (!success) {
			throw new IOException("Build error. Build success not found. See log for the mvn command resume log");
		}
		return success;
	}
}
