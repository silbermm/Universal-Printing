package org.simoes.lpd.handler;

import org.simoes.lpd.common.*;

/**
 * Defines the interface for any class looking to process
 * printJobs that are in the PrintQueue.  The QueueMonitor
 * passes work to this Interface.
 * 
 * @author Chris Simoes
 */
public interface HandlerInterface {

	/**
	 * Processes the PrintJob in some manner.
	 * @param printJob the PrintJob to process
	 * @return the result of the processing
	 */
	public boolean process(PrintJob printJob);
}
