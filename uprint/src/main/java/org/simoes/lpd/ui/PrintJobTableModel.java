package org.simoes.lpd.ui;

import org.simoes.lpd.util.*;
import org.simoes.util.*;

import java.util.*;
import javax.swing.table.*;

import org.apache.log4j.Logger;

/**
 * Represents the data our UI will show.
 * It wraps around the PrintQueue data store.
 * Thus, setPrintQueueDataModel() must be called 
 * before this TableModel is used by the View.
 *   
 * @author Chris Simoes
 *
 */
public class PrintJobTableModel extends AbstractTableModel {
	static Logger log = Logger.getLogger(PrintJobTableModel.class);
 
    private PrintQueue printQueue;
    private String[] headers = {"Job Name", "Job Number", "Size", "Date", "Owner"};
    
    /**
     * Default Construtor.
     *
     */
    public PrintJobTableModel() {
		super();
    }
    
    /**
     * This method must be called prior to using this DataModel.
     * The data for our Model is really stored in a {@link PrintQueue}.
     * @param pq the PrintQueue that holds our real printJob data.
     */
    public void setPrintQueueDataModel(PrintQueue pq) {
    	printQueue = pq;
    }

	/**
	 * Removes all printJobs in our model.
	 * @return the number of rows removed.
	 */
	public int removeAllRows() {
		int result = printQueue.removeAll(); 
		this.fireTableDataChanged();
		return result;
	}
    
	public int getRowCount() {
		return printQueue.list().size();
	}
	
	public int getColumnCount() {
		return headers.length;
	}
	
	public String getColumnName(int column) {
		String result = new String();
		if(column >= 0 && column < getColumnCount()) {
			result = headers[column];
		}
		return result;
	}
	
	public Object getValueAt(int row, int column) {
		final String METHOD_NAME = "getValueAt(): "; 
		Object result = null;
		List printJobs = printQueue.list();
		// error checking
		if(row >= getRowCount()) {
			log.warn(METHOD_NAME + "requested row: " + row + ", only " + getRowCount() + " rows available");
			log.warn(METHOD_NAME + "The PrintJob was removed from the queue before we could show it.");
			//throw new ArrayIndexOutOfBoundsException("requested row: " + row + ", only " + getRowCount() + " rows available");
		} else if(column >= getColumnCount()) {
			log.warn(METHOD_NAME + "The PrintJob was removed from the queue before we could show it.");
			log.warn(METHOD_NAME + "requested column: " + column + ", only " + getColumnCount() + " columns available");
			//throw new ArrayIndexOutOfBoundsException("requested column: " + column + ", only " + getColumnCount() + " columns available");
		} else {
			// get row
			QueuedPrintJobInfo qpji = (QueuedPrintJobInfo) printJobs.get(row); 
			
			// get column data
			switch(column) {
				case 0: // job name 
					result = qpji.getName();
					break;
				case 1: // print job number 
					result = String.valueOf(qpji.getId());
					break;
				case 2: // size 
					result = String.valueOf(qpji.getSize());
					break;
				case 3: // date 
					result = DateUtil.createDateString(qpji.getTimeStamp());
					break;
				case 4: // owner 
					result = qpji.getOwner();
					break;
				default:
					throw new ArrayIndexOutOfBoundsException("requested column: " + column + ", only " + getColumnCount() + " columns available");
			}
		}			
		return result;
	}
    
}