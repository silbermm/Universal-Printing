package org.simoes.lpd.util;

import org.apache.log4j.Logger;

import org.simoes.lpd.common.PrintJob;
import org.simoes.lpd.exception.ObjectNotFoundException;
import org.simoes.lpd.ui.PrintJobTableModel;
import org.simoes.lpd.util.Queue;

/**
 * The PrintQueue is just a queue that only accepts print jobs
 * still need to figure out how the events get sent
 * 
 * @author Chris Simoes
 */
public class PrintQueueWithTableModel extends PrintQueue {
    
	// logger
	static Logger log = Logger.getLogger(PrintQueueWithTableModel.class);
    
	// private constants
	private final static String CLASS = PrintQueueWithTableModel.class.getName();
	private final static String NEWLINE = System.getProperty("line.separator");
    
    private PrintJobTableModel printJobTableModel;
    
	/** constructor.
	 * @param name - name of the print queue.
	 */
	public PrintQueueWithTableModel(String name, PrintJobTableModel printJobTableModel) {
		super(name);
		this.printJobTableModel = printJobTableModel;
	}
    
	/** add print job to queue.
	 * @return the unique identifier of the print job in the queue.
	 */
	public long add(PrintJob printJob) {
		long result = queue.add(printJob);
		printJobTableModel.fireTableDataChanged();
		return result;
	}
    
	/** remove print job from queue.
	 * @return print job from the queue or null if it was not found
	 */
	public PrintJob remove(long id) {
		PrintJob rval = null;
		try {
			rval = (PrintJob)queue.remove(id);
		} catch (ObjectNotFoundException objectNotFoundException) {
			log.info("print job for id[" + id + "] was not found in the queue[" + queue.getName() + "]");
		}
		printJobTableModel.fireTableDataChanged();
		return rval;
	}
    
	/** remove all print jobs from queue.
	 * @return number of jobs removed, or -1 if this failed
	 */
	public int removeAll() {
		int rval = -1;
		rval = size();
		queue = new Queue(queue.getName());
		printJobTableModel.fireTableDataChanged();
		return rval;
	}

}
