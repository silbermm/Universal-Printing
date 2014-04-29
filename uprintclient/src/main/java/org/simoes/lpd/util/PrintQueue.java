
package org.simoes.lpd.util;

import java.util.*;

import org.apache.log4j.Logger;

import org.simoes.lpd.common.Lock;
import org.simoes.lpd.common.PrintJob;
import org.simoes.lpd.exception.ObjectNotFoundException;
import org.simoes.lpd.util.Queue;
import org.simoes.lpd.util.QueuedPrintJobInfo;

/**
 * The PrintQueue is just a queue that only accepts print jobs
 * still need to figure out how the events get sent
 * 
 * @author Jason Crowe
 */
public class PrintQueue {
    
    // logger
    static Logger log = Logger.getLogger(PrintQueue.class);
    
    // private constants
    private final static String CLASS = PrintQueue.class.getName();
    private final static String NEWLINE = System.getProperty("line.separator");
    
    // protected variables
    protected final Lock lock;
    protected Queue queue;
    
    /** constructor.
     * @param name - name of the print queue.
     */
    public PrintQueue(String name) {
		queue = new Queue(name);
		lock = new Lock(name);
    }
    
    public Lock getLock() {
    	return lock;
    }
    
    /** add print job to queue.
     * @return the unique identifier of the print job in the queue.
     */
    public long add(PrintJob printJob) {
		return queue.add(printJob);
    }
    
    /** remove print job from queue.
     * @return PrintJob from the queue or null if it was not found
     */
    public PrintJob remove(long id) throws ObjectNotFoundException {
		PrintJob rval = null;
		try {
		    rval = (PrintJob)queue.remove(id);
		} catch (ObjectNotFoundException objectNotFoundException) {
		    log.info("print job for id[" + id + "] was not found in the queue[" + queue.getName() + "]");
		    throw objectNotFoundException;
		}
		return rval;
    }
    
	/**
	 * Gets the next PrintJob without removing it.
     * @return PrintJob the next printJob in the queue or null if theres an error
	 */
	public QueuedPrintJob getNextPrintJob() throws ObjectNotFoundException {
		final String METHOD_NAME = "getNextPrintJob(): "; 
		QueuedPrintJob rval = null;
		try {
			QueuedObject queuedObject = (QueuedObject)queue.getNext();
			long queueId = queuedObject.getId();
			PrintJob printJob = (PrintJob)queuedObject.getObject();
			// create a clone of the PrintJob for our return value
			rval = new QueuedPrintJob(queueId, (PrintJob)printJob.clone()); 
		} catch (ObjectNotFoundException e) {
			log.info(METHOD_NAME + e.getMessage()); 
			throw e;
		}
		return rval;
	}

	/** remove all print jobs from queue.
	 * @return number of jobs removed, or -1 if this failed
	 */
	public int removeAll() {
		int rval = -1;
		rval = size();
		queue = new Queue(queue.getName());
		return rval;
	}

    /** size of the print queue.
     * @return size of the print queue.
     */
    public int size() {
		return queue.size();
    }
    
    /** list items in queue.
     * @throws ClassCastException if queue contains non print jobs.
     */
    public List list() {
		List rval = null;
		
		List list = new Vector();
		// get the items from the queue
		List qlist = queue.list();
		// iterate over them
	 	for (Iterator iter = qlist.iterator(); iter.hasNext();) {
		    QueuedObject queuedObject = (QueuedObject)iter.next();
		    // create queued print job info object
		    QueuedPrintJobInfo queuedPrintJobInfo =
			new QueuedPrintJobInfo(queuedObject);
		    // add to list
		    list.add(queuedPrintJobInfo);
		}
		rval = list;
		return rval;
    }
    
    public void closeQueue() {
    	this.notifyAll();
    }
}
