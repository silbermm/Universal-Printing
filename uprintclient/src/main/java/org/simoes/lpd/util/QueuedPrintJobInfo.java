/**
 * represents a queued print job's information.
 * provides string representation and easy access to vital info/attributes
 * of a queued print job.
 */
package org.simoes.lpd.util;

import java.text.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.simoes.util.*;
import org.simoes.lpd.common.*;
import org.simoes.lpd.util.QueuedObject;

/**
 * Encapsulates the PrintJob that is stored in the Queue.
 * A copy of the actual stored PrintJob is returned.  This class
 * makes it easy to expose more attibutes of the Print Job
 * as necessary. 
 * 
 * @author Jason Crowe
 */
public class QueuedPrintJobInfo {
    
    // logger
    static Logger log = Logger.getLogger(QueuedPrintJobInfo.class);
    
    // private constants
    private final static String CLASS = QueuedPrintJobInfo.class.getName();
    private final static String NEWLINE = System.getProperty("line.separator");
    
    // attributes
    private long id;
    private Date timeStamp;
    private int size;
    private String name;
    private String owner;
    
    /** constructor.
     * @param queuedObject - a queued print job.
     * @throws ClassCastException if the queueObject isn't a queue print job.
     */
    public QueuedPrintJobInfo(QueuedObject queuedObject) {
        final String METHOD = "QueuedPrintJobInfo(QueuedObject queuedObject)";
        // log entry
        //log.debug("entry[" + METHOD + "]" + NEWLINE + 
        //         "  queuedObject...[" + queuedObject + "]");
        
        this.id = queuedObject.getId();
        this.timeStamp = queuedObject.getTimeStamp();
        PrintJob printJob = (PrintJob)queuedObject.getObject(); // can throw ClassCastException
        this.size = printJob.getSize();
        this.name = printJob.getName();
        this.owner = printJob.getOwner();
        
        // log exit
        //log.debug("exit[" + METHOD + "]");
    }
    
    // getters
    public long getId() {
		return id;
    }
    public Date getTimeStamp() {
		return (Date)timeStamp.clone();
    }
    public int getSize() {
		return size;
    }
    public String getName() {
		return name;
    }
    public String getOwner() {
		return owner;
    }
    
    // setters
    // [none, only the constructor does assignments]

    // TODO: provide static methods to convert list of QueuedPrintJobInfo objects
    // TODO:  into hashtables indexed by each of the attributes OR
    // TODO:  into lists sorted by the attribute
    
    // one line representation (for text ui output)
    // (convenience method)
    public static String singleLine(QueuedPrintJobInfo o) {
		return o.getId() + ", " + o.getName() + ", " + o.getTimeStamp() + ", " + o.getSize() + ", " + o.getOwner();
    }
    
	public String getShortDescription() {
		DecimalFormat df = new DecimalFormat("000");
		String jobId = df.format(id);
		
		StringBuffer sb = new StringBuffer();
		// job id
		sb.append(StringUtil.createFixedLengthString(jobId, Constants.JOB_ID_LENGTH));
		// job name
		sb.append(StringUtil.createFixedLengthString(name, Constants.JOB_NAME_LENGTH));
		// job owner
		sb.append(StringUtil.createFixedLengthString(owner, Constants.JOB_OWNER_LENGTH));
		return sb.toString();
	}

    public String getLongDescription() {
		// move these to properties file
		DecimalFormat df = new DecimalFormat("000");
		String jobId = df.format(id);
		String jobSize = df.format(size);
		
		StringBuffer sb = new StringBuffer();

		// job id
		sb.append(StringUtil.createFixedLengthString(jobId, Constants.JOB_ID_LENGTH));
		// job name
		sb.append(StringUtil.createFixedLengthString(name, Constants.JOB_NAME_LENGTH));
		// job owner
		sb.append(StringUtil.createFixedLengthString(owner, Constants.JOB_OWNER_LENGTH));
		// print time
		sb.append(StringUtil.createFixedLengthString(DateUtil.createDateString(timeStamp), Constants.JOB_DATE_LENGTH));
		// job size
		sb.append(StringUtil.createFixedLengthString(jobSize, Constants.JOB_SIZE_LENGTH));
		return sb.toString();
    }
    
    // string representation
    public String toString() {
    	// move these to properties file
    	final int JOB_NAME_LENGTH = 10;
		final int OWNER_LENGTH = 10;
		DecimalFormat df = new DecimalFormat("000");
		String jobId = df.format(id);
		String jobSize = df.format(size);
		
        StringBuffer sb = new StringBuffer();
		// job id
		sb.append("Job Id [");
		sb.append(jobId);
		sb.append("] ");
		// job name
		sb.append("Name [");
		sb.append(StringUtil.createFixedLengthString(name, JOB_NAME_LENGTH));
		sb.append("] ");
		// job size
		sb.append("Size [");
		sb.append(jobSize);
		sb.append("] ");
		// job owner
		sb.append("Owner [");
		sb.append(StringUtil.createFixedLengthString(owner, OWNER_LENGTH));
		sb.append("] ");
		// print time
		sb.append("Date [");
		sb.append(DateUtil.createDateString(timeStamp));
		sb.append("] ");
		return sb.toString();
    }
}
