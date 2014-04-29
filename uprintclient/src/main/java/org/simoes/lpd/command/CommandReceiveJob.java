package org.simoes.lpd.command;

import org.simoes.lpd.common.*;
import org.simoes.lpd.exception.*;
import org.simoes.lpd.util.*;
import org.simoes.util.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * 
 * This class handles the Receive a printer job Command in RFC1179. 
 * The RFC description is below:
 * <BR>
 * 5.2 02 - Receive a printer job
 * <BR>
 *       +----+-------+----+<BR>
 *       | 02 | Queue | LF |<BR>
 *       +----+-------+----+<BR>
 *       Command code - 2<BR>
 *       Operand - Printer queue name<BR>
 * <BR>
 *    Receiving a job is controlled by a second level of commands.  The
 *    daemon is given commands by sending them over the same connection.
 *    The commands are described in the next section (6).
 * <BR>
 *    After this command is sent, the client must read an acknowledgement
 *    octet from the daemon.  A positive acknowledgement is an octet of
 *    zero bits.  A negative acknowledgement is an octet of any other
 *    pattern.
 * 
 *  @author Chris Simoes
 *
 */
public class CommandReceiveJob extends CommandHandler {
	static Logger log = Logger.getLogger(CommandReceiveJob.class);

	/**
	 * Constructor for CommandReceiveJob.  
	 * @param command the lpr client command, 
	 * 		it should start with a 0x2 if this command was called
	 * @param is the InputStream from the lpr client
	 * @param os the OutputStream to the lpr client
	 */
	public CommandReceiveJob(byte[] command, InputStream is, OutputStream os) {
		super(command, is, os);
	}
	
	/**
	 * Receives the print job and adds it to the queue.
	 * @throws LPDException thrown when an error occurs
	 */
	public void execute() throws LPDException {
		final String METHOD_NAME = "execute(): ";
		
		Vector info = StringUtil.parseCommand(command);
		if(null != info && info.size() > 1) {
			byte[] cmd = (byte[]) info.get(0);
			byte[] queue = (byte[]) info.get(1);
			String queueName = (new String(queue)).trim(); // parseCommand() should guarantee queue is not null
			// receive job command
			if(0x2 != cmd[0]) {
				throw new LPDException(METHOD_NAME + "command passed in was bad, cmd[0]=" + new String(cmd));
			} else if(StringUtil.isEmpty(queueName)) {
				throw new LPDException(METHOD_NAME + "queueName passed in was empty for command=" + new String(command));
			} 
			try {
				os.write(Constants.ACK); // write ACK to client
				log.debug(METHOD_NAME + "Receive Job Command");
				PrintJob job = receivePrintJob(is, os);

				Queues.getInstance().addPrintJob(queueName, job);
			} catch(QueueException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new LPDException(METHOD_NAME + e.getMessage());
			} catch(IOException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new LPDException(METHOD_NAME + e.getMessage());
			}
		} else {
			throw new LPDException(METHOD_NAME + "command not understood, command=" + new String(command));
		}
	}

	/**
	 * Does the work of receiving the print job.
	 * @param is
	 * @param os
	 * @return
	 * @throws LPDException
	 */
	private PrintJob receivePrintJob(InputStream is, OutputStream os) 
		throws LPDException
	{
		final String METHOD_NAME = "receivePrintJob(): ";
		PrintJob printJob = null;
		ControlFile controlFile = null;
		DataFile dataFile = null;
	
		try
		{
			NetUtil netUtil = new NetUtil();
			byte[] receiveInput = null;
			Vector cmd = null;
			
			for (int i=1; i<=2; i++)
			{
				receiveInput = netUtil.readNextInput(is, os);
				cmd = StringUtil.parseCommand(receiveInput);
				if (receiveInput[0] == 2) {
					controlFile = setControlFile(is,os,cmd);
				} else if(receiveInput[0] == 3) {
					dataFile = setDataFile(is,os,cmd);
				}
			}
		} catch(Exception ex) {
			log.error(METHOD_NAME + "problems reading Input");
		}
	
		if(null != controlFile && null != dataFile) {
			printJob = new PrintJob(controlFile, dataFile); 
		}
		return printJob;
	}

	/**
	 * Receives the ControlFile
	 * @param is InputStream containing the ControlFile after the header
	 * @param os OutputStream containing the ControlFile after the header
	 * @param cmd the initial header to the controlFile
	 * @return the ControlFile object populated
	 * @throws LPDException
	 */
	private ControlFile setControlFile(InputStream is, OutputStream os, Vector cmd) throws LPDException
	{
		final String METHOD_NAME = "setControlFile(): ";
		ControlFile controlFile = null;

		// get the control file
		try {
			NetUtil netUtil = new NetUtil();
			String controlFileSize = new String((byte[]) cmd.get(1));
			String controlFileHeader = new String((byte[]) cmd.get(2));
			Vector headerVector = StringUtil.parsePrintFileName(controlFileHeader);
			if(null != headerVector && headerVector.size() == 3) {
				byte[] cFile = netUtil.readControlFile(is, os);
				controlFile = new ControlFile();
				controlFile.setCount(controlFileSize);
				controlFile.setJobNumber((String) headerVector.get(1));
				controlFile.setHostName((String) headerVector.get(2));
				controlFile.setContents(cFile);
				log.debug(METHOD_NAME + "Control File=" + new String(cFile));
				controlFile.setControlFileCommands(cFile);
				log.debug(METHOD_NAME + "Control File Commands=" + controlFile.getControlFileCommands().toString());
				return controlFile;
			} else {
				throw new LPDException(METHOD_NAME + "controlFileHeader did not parse properly, controlFileHeader=" + controlFileHeader);
			}
		} catch(IOException e) {
			log.error(METHOD_NAME + "Had trouble receiving the control file.");
			log.error(METHOD_NAME + e.getMessage());
			throw new LPDException(e);
		}
	}

	/**
	 * Receives the DataFile
	 * @param is InputStream containing the DataFile after the header
	 * @param os OutputStream containing the DataFile after the header
	 * @param cmd the initial header to the dataFile
	 * @return the DataFile object populated
	 * @throws LPDException
	 */
	private DataFile setDataFile(InputStream is, OutputStream os, Vector cmd) throws LPDException
	{
		final String METHOD_NAME = "setDataFile(): ";
		DataFile dataFile = null;

		// get the data file
		try
		{
			NetUtil netUtil = new NetUtil();
			String dataFileSize = new String((byte[]) cmd.get(1));
			log.info(METHOD_NAME + "DataFile size=" + dataFileSize);
			String dataFileHeader = new String((byte[]) cmd.get(2));
			Vector headerVector = StringUtil.parsePrintFileName(dataFileHeader);
			if(null != headerVector && headerVector.size() == 3) {
			byte[] dFile = null;
			int dfSize = 0;
			try {
				dfSize = Integer.parseInt(dataFileSize);
			} catch(NumberFormatException e) {
				log.error(METHOD_NAME + e.getMessage());
			}
			if(0 == dfSize) {
				dFile = netUtil.readPrintFile(is, os);
			} else {
				dFile = netUtil.readPrintFile(is, os, dfSize);
			}
			dataFile = new DataFile();
			dataFile.setCount(dataFileSize);
			dataFile.setHostName((String) headerVector.get(2));
			dataFile.setJobNumber((String) headerVector.get(1));
			dataFile.setContents(dFile);
			//log.debug(METHOD_NAME + "Data File=" + new String(dFile));
			return dataFile;
			} else {
				throw new LPDException(METHOD_NAME + "dataFileHeader did not parse properly, dataFileHeader=" + dataFileHeader);
			}
		} catch(IOException e) {
			log.error(METHOD_NAME + "Had trouble receiving the data file.");
			log.error(METHOD_NAME + e.getMessage());
			throw new LPDException(e);
		}
	}

}