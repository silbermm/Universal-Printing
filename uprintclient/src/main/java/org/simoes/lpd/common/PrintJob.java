package org.simoes.lpd.common;

import org.simoes.lpd.exception.*;

import org.apache.log4j.Logger;

/**
 * Represents any print jobs sent to us.  It holds the {@link ControlFile}
 * and the {@link DataFile} that make up the print job.  Convience methods
 * are also provided to get commonly used information such as jobName.   
 * 
 * @author Chris Simoes
 *
 *
 */
public class PrintJob implements Cloneable {
	static Logger log = Logger.getLogger(PrintJob.class);

	private ControlFile controlFile;
	private DataFile dataFile;
	
	/**
	 * Constructor requires a ControlFile and a DataFile.
	 * @param cf
	 * @param df
	 * @throws LPDException
	 */
	public PrintJob(ControlFile cf, DataFile df)
		throws LPDException  
	{
		super();
		this.controlFile = cf;
		this.dataFile = df;
	}
	
	public String getName() {
		return this.controlFile.getControlFileCommands().getJobName();
	}
	public int getSize() {
		return this.dataFile.getContents().length;
	}
	public String getOwner() {
		return this.controlFile.getControlFileCommands().getUserId();
	}
	
	public ControlFile getControlFile() {
		return this.controlFile;
	}
	public DataFile getDataFile() {
		return this.dataFile;
	}

	public Object clone() {
		final String METHOD_NAME = "clone()";
		PrintJob result = null;
		try {
			result = (PrintJob)super.clone();
			result.controlFile = (ControlFile)controlFile.clone();
			result.dataFile = (DataFile)dataFile.clone();
		} catch(CloneNotSupportedException e) {
			log.error(METHOD_NAME + e.getMessage());
			throw new InternalError(METHOD_NAME + e.getMessage());
		}
		return result;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(null != this.controlFile) {
			sb.append(", ControlFile:\n");
			sb.append(controlFile.getContents());
		} else {
			sb.append(", controlFile is null");
		}
		if(null != this.dataFile) {
			sb.append(", DataFile:\n");
			sb.append(dataFile.getContents());
		} else {
			sb.append(", dataFile is null");
		}

		return sb.toString();
	}
	
}