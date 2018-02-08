package com.feature.deployer.command.services;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

public interface CommandService {
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
	public Hashtable<String, Object> bufferedReaderLinesForCommandLine(String cmdline,
			String message, boolean okMessage, 
			String operation, boolean interrupt) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
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
	public List<String> bufferedReaderLinesForCommandLine(String cmdline) throws IOException, InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;
	
	
}
