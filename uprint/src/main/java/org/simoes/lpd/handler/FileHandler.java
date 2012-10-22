package org.simoes.lpd.handler;

import org.simoes.lpd.common.*;
import org.simoes.util.*;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Handles PrintJobs by writing them to File.
 * 
 * @author Chris Simoes
 */
public class FileHandler implements HandlerInterface {
	static Logger log = Logger.getLogger(FileHandler.class);

	public FileHandler() {
		super();
	}
	
        
	/**
	 * Writes the printJob to disk using the jobName and jobId.
	 * @param printJob the PrintJob we are processing
	 * @return the result of our work, true for success or false for non-success
	 */
	public boolean process(PrintJob printJob) {
		final String METHOD_NAME = "process(): ";
		boolean result = false;
		if(null != printJob 
			&& null != printJob.getControlFile()
			&& null != printJob.getDataFile()) 
		{
			// create file name, pjb == print job
			String fileName = "/tmp/" + printJob.getName() + printJob.getControlFile().getJobNumber() + ".ps";
			try { 
				FileUtil.writeFile(printJob.getDataFile().getContents(), fileName);
				result = true;
			} catch(IOException e) {
				log.error(METHOD_NAME + e.getMessage());
			}
		} else {
			log.error(METHOD_NAME + "The printJob or printJob.getControlFile() or printJob.getDataFile() were empty");
		}
		return result;
	}

}
