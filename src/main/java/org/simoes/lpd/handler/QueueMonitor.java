package org.simoes.lpd.handler;

import com.typesafe.config.Config;
import org.simoes.lpd.common.*;
import org.simoes.lpd.exception.QueueException;
import org.simoes.lpd.util.*;

import org.apache.log4j.Logger;
import silbersoft.ucprint.ui.PrintView;
import silbersoft.ucprint.ui.models.PrintButtonModel;
import silbersoft.ucprint.ui.models.PrintViewListModel;

/**
 * Watches the PrintQueue and removes jobs as they appear there.
 * The jobs are passed on to the proper handler.
 * 
 * @author Chris Simoes
 */

public class QueueMonitor implements Runnable{

	static Logger log = Logger.getLogger(QueueMonitor.class);

	private boolean run;
	private String name;
	private PrintQueue printQueue;
	private Lock queueLock;
	private HandlerFactory handlerFactory;
	
	/**
	 * Create a QueueMonitor.
	 * @param queueName the queue we will be monitoring.
	 */
	public QueueMonitor(String queueName, Config config, PrintButtonModel printButton, PrintViewListModel buildingList, PrintView printerView) throws QueueException {
		super();
		run = true;
		name = queueName;
		queueLock = Queues.getInstance().getQueueLock(queueName);
		handlerFactory = HandlerFactory.getInstance(config, printButton, buildingList, printerView); 
	}
	
	public void stop() {
		run = false;
	}
	
	public String getQueueName() {
		return name;
	}
	
	/**
	 * Watches the PrintQueue and process PrintJobs as they become
	 * available.  If there is an error processing a PrintJob then
	 * The PrintJob is left in the PrintQueue.
	 */ 
	public void run() {
		final String METHOD_NAME = "run(): ";
		
		QueuedPrintJob currentJob = null;
		try {
			while(run) {
				synchronized(queueLock) {
					// start spin lock
					while(0 == Queues.getInstance().getQueueSize(name)) {
						try {
							log.debug(METHOD_NAME + "Nothing to process going to wait()");
							queueLock.wait();
							log.debug(METHOD_NAME + "Somebody woke me up, going to check the Queue: " + name);
						} catch(InterruptedException e) {}  // ignored on purpose
					}
					// if queue has printJob(s) then process them
					currentJob = Queues.getInstance().getNextPrintJob(name);
				} // end synchronized
				// now process job outside of the synchronized loop
				HandlerInterface handler = handlerFactory.getPrintHandler();
				if(handler.process(currentJob.getPrintJob())) {
					// remember to delete if nothing goes wrong.
					String userName = currentJob.getPrintJob().getOwner();
					String jobId = String.valueOf(currentJob.getJobId());
					Queues.getInstance().removePrintJob(name, userName, jobId);
				} else {
					log.error(METHOD_NAME + "Error trying to process: " + currentJob.toString());
				}
			}
		} catch(QueueException e) {
			log.error(METHOD_NAME + e.getMessage());
			log.fatal(METHOD_NAME + "The error above killed the QueueMonitor for:" + name);
		}
	}
}
