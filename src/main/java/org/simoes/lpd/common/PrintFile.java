package org.simoes.lpd.common;

import org.apache.log4j.Logger;

/**
 * 
 * The abstract super-class that represents the 2 files sent to us
 * from the CommandReceiveJob.  
 * @author Chris Simoes
 *
 *
 */
public abstract class PrintFile {
	static Logger log = Logger.getLogger(PrintFile.class);

	private String jobNumber;
	private String hostName;
	private String count;
	private byte[] contents;
	
	public void setJobNumber(String a) {
		this.jobNumber = a;
	}
	public void setHostName(String a) {
		this.hostName = a;
	}
	public void setCount(String a) {
		this.count = a;
	}
	public void setContents(byte[] a) {
		this.contents = a;
	}
	
	public String getJobNumber() {
		return this.jobNumber;
	}
	public String getHostName() {
		return this.hostName;
	}
	public String getCount() {
		return this.count;
	}
	public byte[] getContents() {
		return this.contents;
	}
	
}