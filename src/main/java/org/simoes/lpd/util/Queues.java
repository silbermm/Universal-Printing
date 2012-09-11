package org.simoes.lpd.util;

import java.util.*;
import org.simoes.lpd.common.*;
import org.simoes.lpd.exception.*;
import org.simoes.lpd.ui.*;
import org.simoes.util.*;
import org.apache.log4j.Logger;

/**
 * This class maintains all of the Print queues and contains all of the logic
 * for performing operations on them.     
 * 
 * @author Chris Simoes
 *
 */
public class Queues {
	static Logger log = Logger.getLogger(Queues.class);

	private final static Queues INSTANCE = new Queues();
	private final Hashtable queues = new Hashtable(); 
	
	/**
	 * Constructor for LPD.
	 */
	private Queues() {
		super();
		log.debug("Queues(): STARTED");
	}

	/**
	 * This class is a singleton.  
	 * @return the only instance of Queues.
	 */
	public static Queues getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Creates a Print Queue with the name passed in
	 * @param queueName - the name of the print queue
	 * @throws QueueException
	 */
	public void createQueue(String queueName) throws QueueException {
		final String METHOD_NAME = "createQueue(): ";
		if(StringUtil.isEmpty(queueName)) {
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(queues.containsKey(queueName)) {
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") already exists.");
		} else {
			queues.put(queueName, new PrintQueue(queueName));
		}
	}
	
	/**
	 * Creates a Print Queue with the name passed in and creates a TableModel for the GUI.
	 * @param queueName - the name of the print queue
	 * @param tableModel - the TableModel passed to the View
	 * @return a PrintQueue object that was just created.
	 * @throws QueueException
	 */
	public PrintQueue createQueueWithTableModel(String queueName, PrintJobTableModel tableModel) throws QueueException {
		final String METHOD_NAME = "createQueue(): ";
		if(StringUtil.isEmpty(queueName)) {
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(queues.containsKey(queueName)) {
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") already exists.");
		} else {
			PrintQueue queue = new PrintQueueWithTableModel(queueName, tableModel);
			queues.put(queueName, queue);
			return queue;
		}
	}

	/**
	 * Adds a printJob to the queue specified by queueName.
	 * @param queueName - the name of the print queue
	 * @param printJob - the printJob added to the print queue
	 * @throws QueueException
	 */
	public void addPrintJob(String queueName, PrintJob printJob) throws QueueException {
		final String METHOD_NAME = "addPrintJob(): ";
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else if(null == printJob) {
			log.error(METHOD_NAME + "PrintJob passed in was null.");
			throw new QueueException(METHOD_NAME + "PrintJob passed in was null.");
		} else if(null == printJob.getDataFile()) {
			log.error(METHOD_NAME + "DataFile in PrintJob passed in was null.");  
			throw new QueueException(METHOD_NAME + "DataFile in PrintJob passed in was null.");
		} else if(null == printJob.getDataFile().getContents()) {
			log.error(METHOD_NAME + "contents of DataFile in PrintJob passed in was null.");  
			throw new QueueException(METHOD_NAME + "contents of DataFile in PrintJob passed in was null.");
		} else {
			PrintQueue queue = (PrintQueue) queues.get(queueName);
			log.debug(METHOD_NAME + "got queue: " + queueName);  
			Lock lock = queue.getLock();
			log.debug(METHOD_NAME + "got lock for queue: " + queueName);  
			synchronized(lock) {
				queue.add(printJob);
				lock.notifyAll();
				log.debug(METHOD_NAME + "sent notify all to Lock object for queue: " + queueName);  
			}
		}
	}

	/**
	 * Removes a printJob based on queueName and the jobNumber.
	 * @param queueName - the name of the print queue
	 * @param user - the user trying to delete the print job 
	 * @param jobNumber - the print job number assigned by the Queue
	 * @throws QueueException
	 */
	public void removePrintJob(String queueName, String user, String jobNumber) throws QueueException {
		final String METHOD_NAME = "removePrintJob(): ";
		long jobId = 0;
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else if(StringUtil.isEmpty(user)) {
			log.error(METHOD_NAME + "user passed in was empty.");
			throw new QueueException(METHOD_NAME + "user passed in was empty.");
		} else if(StringUtil.isEmpty(jobNumber)) {
			log.error(METHOD_NAME + "PrintJob passed in was empty.");
			throw new QueueException(METHOD_NAME + "PrintJob passed in was empty.");
		} else {
			try {
				jobId = Long.parseLong(jobNumber);
			} catch(NumberFormatException e) {
				log.warn(METHOD_NAME + "The jobNumber(" + jobNumber + ") passed in was not a number: ");
				log.error(METHOD_NAME + e.getMessage());
				throw new QueueException(METHOD_NAME + e.getMessage());
			}

			PrintQueue queue = (PrintQueue) queues.get(queueName);
			Lock lock = queue.getLock();

			try {
				// if user name is root or Administrator just delete
				if(Constants.USER_ROOT.equals(user.trim())
					|| Constants.USER_ADMINISTRATOR.equalsIgnoreCase(user.trim()))
				{
					synchronized(lock) {
						queue.remove(jobId);
					}
				} else {  // if not then only delete if user and job number match
					List jobs = queue.list();
					Iterator iter = jobs.iterator();
					while(iter.hasNext()) {
						QueuedPrintJobInfo qpji = (QueuedPrintJobInfo) iter.next();
						if(qpji.getId() == jobId) { // match jobId
							String jobOwner = qpji.getOwner();
							if(null != jobOwner && jobOwner.trim().equals(user.trim())) { //match username
								synchronized(lock) {
									queue.remove(jobId);// if username and jobId match, then remove
								}
							}
							// break here?
						}
					}
				}
			} catch(ObjectNotFoundException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new QueueException(e);
			}
		}
	}

	/**
	 * Deletes all printJobs for this queue.
	 * @param queueName - the name of the print queue
	 * @throws QueueException
	 */
	public void removeAllPrintJobs(String queueName) throws QueueException {
		final String METHOD_NAME = "removeAllPrintJobs(): ";
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else {
			PrintQueue queue = (PrintQueue) queues.get(queueName);
			Lock lock = queue.getLock();
			synchronized(lock) {
				queue.removeAll();
			}
		}
	}

	/**
	 * Returns a {@link List} of all print jobs in the Queue. 
	 * @param queueName the name of the queue we are wanting to list
	 * @return a List of all print jobs in the Queue
	 * @throws QueueException thrown if we have problems getting the queue list
	 */
	public List listAllPrintJobs(String queueName) throws QueueException {
		final String METHOD_NAME = "listAllPrintJobs(): ";
		List result = null;
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else {
			PrintQueue queue = (PrintQueue) queues.get(queueName);
			Lock lock = queue.getLock();
			synchronized(lock) {
				result = queue.list();
			}
		}
		return result;
	}
	
	public int getQueueSize(String queueName) throws QueueException {
		final String METHOD_NAME = "getQueueSize(): ";
		int result = -1;
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else {
			PrintQueue queue = (PrintQueue) queues.get(queueName);
			Lock lock = queue.getLock();
			synchronized(lock) {
				result = queue.size();
			}
		}
		return result;
	}
	
	public Lock getQueueLock(String queueName)throws QueueException {
		final String METHOD_NAME = "getQueueLock(): ";
		Lock result = null;
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else {
			PrintQueue queue = (PrintQueue) queues.get(queueName);
			result = queue.getLock();
		}
		return result;
	}
	
	public QueuedPrintJob getNextPrintJob(String queueName) throws QueueException {
		final String METHOD_NAME = "getNextPrintJob(): ";
		QueuedPrintJob result = null;
		if(StringUtil.isEmpty(queueName)) {
			log.error(METHOD_NAME + "queueName passed in was empty.");
			throw new QueueException(METHOD_NAME + "queueName passed in was empty.");
		} else if(!queues.containsKey(queueName)) {
			log.error(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
			throw new QueueException(METHOD_NAME + "the queue called(" + queueName + ") does not exist.");
		} else {
			PrintQueue queue = (PrintQueue) queues.get(queueName);
			Lock lock = queue.getLock();
			
			try {
				synchronized(lock) {
					result = queue.getNextPrintJob();
				}
			} catch(ObjectNotFoundException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new QueueException(e);
			}
		}
		return result;
	}
}