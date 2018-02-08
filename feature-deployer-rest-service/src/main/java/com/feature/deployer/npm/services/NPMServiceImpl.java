package com.feature.deployer.npm.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feature.deployer.command.services.CommandService;
import com.feature.persistence.services.BashProcessDAO;
import com.feature.persistence.services.BashProcessDAOImpl;
import com.feature.persistence.services.CurrentFrontDeployProcessDAO;
import com.feature.persistence.services.CurrentFrontDeployProcessDAOImpl;

import feature.deployer.resources.deploy.DeployFrontResource;
import feature.deployer.resources.deploy.ServerJsParameterResource;
import feature.deployer.resources.mysql.BashProcessResource;
import feature.deployer.resources.mysql.CurrentFrontDeployResource;

@Service("npmService")
public class NPMServiceImpl implements NPMService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NPMServiceImpl.class);
	
	@Autowired
	private CommandService commandService;
	
	private static String SERVER_JS = "server.js";
	private static String SERVER_JS_BACKUP = "server.js.backup";	
	
	
	// OPERATIONS
	private static String OPERATION_NPM_INSTALL = "Npm Install";
	private static String OPERATION_GULP_BUILD = "gulp build";
	private static String OPERATION_NPM_START_DEV = "npm start dev";
	
	
	// DIFFERENT VALUES
	private static String PID_VALUE_HASH = "PID";
	private static String OK_MESSAGE_VALUE_HASH = "OK_MESSAGE";
	private static String SERVER_PORT_VALUE_HASH = "SERVER_PORT";
	private static String SERVER_BACKEND_PORT_VALUE_HASH = "SERVER_BACKEND_PORT";
	
	// MESSAGES
	private static String NPM_ERROR = "npm ERR!";
	private static String GULP_BUILD_OK = "Compilación finalizada con éxito!";
	private static String NPM_START_DEV_OK = "Express initiate in mode: LOCAL in port:"; 

	
	/**
	 * Edit server.js with declared parameters by user.
	 * 
	 * Will edit vars with a new value.
	 * 
	 * 
	 * @author jta010es
	 * 
	 * @param String projectsSourceCodePath
	 * 
	 * @param List<ServerJsParameterResource> serverJsParameters 
	 * 
	 */
	@Override
	public void editServerJs(String projectsSourceCodePath,
			List<ServerJsParameterResource> serverJsParameters) throws IOException, InterruptedException {
		
		
		// Parameters
		Hashtable<String, String> hashParameters = getHashParameters(serverJsParameters);
		
		
		// Origin file (will be edited)
		String serverJS = new StringBuilder(projectsSourceCodePath)
				.append(File.separator)
				.append(SERVER_JS).toString();
		File fileServerJs = new File(serverJS);     
		
				
		// Backup (read)
		String serverJSBackup = new StringBuilder(projectsSourceCodePath)
				.append(File.separator)
				.append(SERVER_JS_BACKUP).toString();
		
		File serverJSBackupFile = new File(serverJSBackup);
		serverJSBackupFile.createNewFile();
		
		
		OutputStream output = new FileOutputStream(serverJSBackupFile);

		
		
		
		// Create backup
		createBackUpFile(serverJS, output);
		

		
		// Writer   
        FileWriter fileWriterServerJs = new FileWriter(fileServerJs);
        BufferedWriter bufferedWriterServerJs = new BufferedWriter(fileWriterServerJs);    

        // Input
		InputStream isServerJsBackup = new FileInputStream(serverJSBackup);
        
		
		
		BufferedReader bufferedReaderServerJsBackup = new BufferedReader(new InputStreamReader(isServerJsBackup));
		
		//var apiVersion = '2.0';
		//var warVersion = '2.0.0.1-SNAPSHOT';
		
		
		String line = null;
		while ((line = bufferedReaderServerJsBackup.readLine()) != null) {
			LOGGER.debug(line);
			
			StringTokenizer tokenizer = new StringTokenizer(line, "=");
			while(tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextElement().toString();
				String tokenTrim = token.replace(" ", "");
				if (tokenTrim.contains("var")) {
					String varFromServerJs = tokenTrim.substring("var".length(), tokenTrim.length());
					
					String varValueFromHash = hashParameters.get(varFromServerJs);
					if (varValueFromHash!=null) {
						line = new StringBuilder("var ")
								.append(varFromServerJs)
								.append(" = '")
								.append(varValueFromHash)
								.append("';").toString();
					}
				}
			}
			
			bufferedWriterServerJs.write(line);
			bufferedWriterServerJs.write("\n");	
			
		}
		
		
		// Close
		bufferedWriterServerJs.flush();
        bufferedWriterServerJs.close();
        
        fileWriterServerJs.close();
        bufferedReaderServerJsBackup.close();
        
        
        isServerJsBackup.close();
        output.close();
	}
	
	
	/**
	 * Execute "npm install" on path
	 * 
	 * @param {@link String} projectsSourceCodePath
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void npmInstall(String projectsSourceCodePath) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		// Create command
		String cmdline = "cd " + projectsSourceCodePath + " && nvm use 7.7.4 && npm install";

		// Execute command	
		commandService.bufferedReaderLinesForCommandLine(cmdline, 
				NPM_ERROR, false, 
				OPERATION_NPM_INSTALL, false);
	}
	
	

	/**
	 * Execute "gulp build" on path
	 * 
	 * @param {@link String} projectsSourceCodePath
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void gulpBuild(String projectsSourceCodePath) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// Create command
		String cmdline = "cd " + projectsSourceCodePath + " && nvm use 7.7.4 && gulp build";

		// Execute command	
		commandService.bufferedReaderLinesForCommandLine(cmdline, 
				GULP_BUILD_OK, true, 
				OPERATION_GULP_BUILD, false);
	}
	
	
	/**
	 * Execute "npm start dev" on path
	 * 
	 * @param {@link String} projectsSourceCodePath
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public void npmStartDev(DeployFrontResource deployFrontResource) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// Create command
		String cmdline = "cd " + deployFrontResource.getProjectsSourceCodePath() + " && nvm use 7.7.4 && npm start dev";

		String projectName = deployFrontResource.getFrontProject().getProjectName();

		
		List<String> nodeEntriesBefore = commandService.bufferedReaderLinesForCommandLine("ps -e | grep node");
		
		// Execute command	
 		Hashtable<String, Object> values = commandService.bufferedReaderLinesForCommandLine(cmdline, 
				NPM_START_DEV_OK, true, 
				OPERATION_NPM_START_DEV, true);
		
		// Get values
		Integer pid = (Integer) values.get(PID_VALUE_HASH);
		values = parseServerPort(values);
		
		
		List<String> nodeEntriesAfter = commandService.bufferedReaderLinesForCommandLine("ps -e | grep node");
		Integer associatedPid = null;
		String associatedProcessName = "node";
		
		for (String nodeEntry : nodeEntriesAfter) {
			if (!nodeEntriesBefore.contains(nodeEntry)) {
				//  9212 pts/21   00:00:00 node
				StringTokenizer tokenizer = new StringTokenizer(nodeEntry, " ");
				String pidToken = tokenizer.nextToken().trim();
				associatedPid = Integer.parseInt(pidToken);
				break;
			}
		}
		
		BashProcessResource bashProcessResource = new BashProcessResource();
		bashProcessResource.setPid(pid);
		bashProcessResource.setProcessName("bash");
		bashProcessResource.setProcessDescription(projectName);
		bashProcessResource.setTypeDescription("NPM START DEV");
		bashProcessResource.setAssociatedPid(associatedPid);
		bashProcessResource.setAssociatedProcessName(associatedProcessName);
		
		BashProcessDAO bashProcessDAO = new BashProcessDAOImpl();
		bashProcessDAO.insert(bashProcessResource);
		
		
		CurrentFrontDeployResource currentFrontDeployResource = new CurrentFrontDeployResource();
		currentFrontDeployResource.setPidBashProcess(pid);
		currentFrontDeployResource.setProjectDescription(deployFrontResource.getFrontProject().getProjectDescription());
		currentFrontDeployResource.setProjectName(projectName);
		currentFrontDeployResource.setServerPort((Integer)values.get(SERVER_PORT_VALUE_HASH));
		currentFrontDeployResource.setBackendServerPort((Integer)values.get(SERVER_BACKEND_PORT_VALUE_HASH));
	
		CurrentFrontDeployProcessDAO currentFrontDeployProcessDAO = new CurrentFrontDeployProcessDAOImpl();
		currentFrontDeployProcessDAO.insert(currentFrontDeployResource);
	}

	
	/**
	 * Create backup file before edit it
	 * 
	 * @author jta010es
	 * 
	 * 
	 * @param fileOrigin
	 * @param output
	 */
	private void createBackUpFile(String fileOrigin, OutputStream output) {
		
		try {
			// Create backup file
			Files.copy(Paths.get(fileOrigin), output);
		} catch (IOException ioe) {
			LOGGER.error("ERROR creating server.js backup file. Exception: {}", ioe.getMessage());			
		}		
	}
	

	
	private Hashtable<String, String> getHashParameters(List<ServerJsParameterResource> serverJsParameters) throws IOException, InterruptedException {
		
		Hashtable<String, String> hashParameters = new Hashtable<>();
		
		for (ServerJsParameterResource serverJsParameter : serverJsParameters) {
			hashParameters.put(serverJsParameter.getParameterName(), serverJsParameter.getParameterValue());
		}
		
		return hashParameters;		
	}
	
	
	private Hashtable<String, Object> parseServerPort(Hashtable<String, Object> values) {
		
		
		// Line expected -> "Express initiate in mode: LOCAL in port: 9002  Backend Port: 8080"
		String okMessage = (String) values.get(OK_MESSAGE_VALUE_HASH);
		
		StringTokenizer tokenizer = new StringTokenizer(okMessage.substring("Express initiate in mode: ".length(), okMessage.length()), ":");
		int counter = 1;
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (counter==2) {
				String serverPort = token.substring(0, token.length() - "Backend Port".length());
				values.put(SERVER_PORT_VALUE_HASH, Integer.parseInt(serverPort.replaceAll(" ", "")));
			} else if (counter==3) {
				values.put(SERVER_BACKEND_PORT_VALUE_HASH, Integer.parseInt(token.replaceAll(" ", "")));
			}
			counter++;
		}
		
		return values;
	}

	
}
