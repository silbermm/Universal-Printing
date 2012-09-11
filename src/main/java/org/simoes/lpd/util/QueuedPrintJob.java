package org.simoes.lpd.util;

import org.simoes.lpd.common.*;

import org.apache.log4j.Logger;

/**
 * A copy of a printJob plus the jobId used by the PrintQueue
 * to store the opriginal copy of this PrintJob.
 * 
 * 
 * @author Chris Simoes
 */
public class QueuedPrintJob {
	static Logger log = Logger.getLogger(QueuedPrintJob.class);

	private long jobId;
	private PrintJob printJob;
	
	/**
	 * Constructor for the QueuedPrintJob.
	 * @param id the PrintQueue id of the PrintJob
	 * @param printJob a copy of the original PrintJob stored in the PrintQueue 
	 */
	public QueuedPrintJob(long id, PrintJob printJob) {
		this.jobId = id;
		this.printJob = printJob;	
	}
	
	public long getJobId() {
		return jobId;
	}
	
	public PrintJob getPrintJob() {
		return printJob;
	}
}