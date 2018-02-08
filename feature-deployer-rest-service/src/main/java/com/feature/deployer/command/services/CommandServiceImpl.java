package com.feature.deployer.command.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("commandService")
public class CommandServiceImpl implements CommandService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandServiceImpl.class);
	
	// DIFFERENT VALUES
	private static String PID_VALUE_HASH = "PID";
	private static String OK_MESSAGE_VALUE_HASH = "OK_MESSAGE";	
	
	
	/**
	 * Execute command and checks process
	 *  
	 * @author jta010es
	 * 
	 * @param cmdline 		->	Command
	 * @param message		->	Check message
	 * @param okMessage		->	Param message is error (okMessage=false) or ok (okMessage=true)?
	 * @param operation		->	Description of the operation
	 * @param interrupt		->	If false, process finished. If true, process will be up until
	 * 	we said no
	 * 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public Hashtable<String, Object> bufferedReaderLinesForCommandLine(String cmdline,
			String message, boolean okMessage, 
			String operation, boolean interrupt) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Hashtable<String, Object> values = new Hashtable<String, Object>();
		
		LOGGER.info("Execute command: {}", cmdline);
		
		boolean success = true;
		
		Process process = new ProcessBuilder(new String[] { "bash", "-lc", cmdline }).redirectErrorStream(true)
				.directory(new File(".")).start();
		
		int pid = getPID(process);
		values.put(PID_VALUE_HASH, pid);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			LOGGER.debug(line);
			if (okMessage && line.contains(message) && interrupt){
				success = true;
				values.put(OK_MESSAGE_VALUE_HASH, line);
				break;
			} else if (okMessage && line.contains(message)) {
				success = true;
			} else if (!okMessage && line.contains(message)) {
				success = false;
			} 
		}
		
		LOGGER.info("Finish command with result {}", Boolean.valueOf(success));
		if (!interrupt && 0 != process.waitFor()) {
			success = false;
		}		
		
		// Depends on result on command, we return ok or an exception
		if (!success) {
			LOGGER.error("{} error. See log for command resume log", operation);
			throw new IOException(operation + " error. See log for command resume log");
		}
		
		
		return values;
	}
	
	
	/**
	 * Execute command and checks process
	 *  
	 * @author jta010es
	 * 
	 * @param cmdline 		->	Command
	 * @param message		->	Check message
	 * @param okMessage		->	Param message is error (okMessage=false) or ok (okMessage=true)?
	 * @param operation		->	Description of the operation
	 * @param interrupt		->	If false, process finished. If true, process will be up until
	 * 	we said no
	 * 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public List<String> bufferedReaderLinesForCommandLine(String cmdline) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		LOGGER.info("Execute command: {}", cmdline);
		
		Process process = new ProcessBuilder(new String[] { "bash", "-lc", cmdline }).redirectErrorStream(true)
				.directory(new File(".")).start();
		
		List<String> entries = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			LOGGER.debug(line);
			entries.add(line);
		}
		
		return entries;
	}

	
	/**
	 * Returns PID for Process 
	 * 
	 * 
	 * @param process
	 * 
	 * @return int (pid)
	 * 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private int getPID(Process process) throws NoSuchFieldException, 
		SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f = process.getClass().getDeclaredField("pid");
	    f.setAccessible(true);
	    return f.getInt(process);
	    
	}
	
}
