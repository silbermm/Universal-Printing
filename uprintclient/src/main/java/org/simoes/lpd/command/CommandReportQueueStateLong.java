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
 * This class handles the Send queue state (long) Command in RFC1179. 
 * The RFC description is below:
 * <BR>
 * 5.4 04 - Send queue state (long)<BR>
 * <BR>
 *       +----+-------+----+------+----+<BR>
 *       | 04 | Queue | SP | List | LF |<BR>
 *       +----+-------+----+------+----+<BR>
 *       Command code - 4<BR>
 *       Operand 1 - Printer queue name<BR>
 *       Other operands - User names or job numbers<BR>
 * <BR>
 *    If the user names or job numbers or both are supplied then only those
 *    jobs for those users or with those numbers will be sent.
 * 
 *    The response is an ASCII stream which describes the printer queue.
 *    The stream continues until the connection closes.  Ends of lines are
 * 
 * @author Chris Simoes
 *
 */
public class CommandReportQueueStateLong extends CommandHandler {
	static Logger log = Logger.getLogger(CommandReportQueueStateLong.class);

	public CommandReportQueueStateLong(byte[] command, InputStream is, OutputStream os) {
		super(command, is, os);
	}
	
	/**
	 * Writes a text table that displays the current print jobs.  This 
	 * is invoked by the commands: lpq or lpstat.  A queue name must
	 * be specified by the client. 
	 * @throws LPDException thrown when an error occurs
	 */
	public void execute() throws LPDException {
		final String METHOD_NAME = "execute(): ";
		
		Vector info = StringUtil.parseCommand(command);
		if(null != info && info.size() > 0) {
			byte[] cmd = (byte[]) info.get(0);
			byte[] queue = (byte[]) info.get(1);
			String queueName = (new String(queue)).trim(); // parseCommand() should guarantee queue is not null
			try {
				// query queue for state
				List queueudPrintJobInfos = Queues.getInstance().listAllPrintJobs(queueName);
				// create a ASCII representation of the List
				StringBuffer sb = new StringBuffer();
				sb.append(StringUtil.createFixedLengthString("Job Id", Constants.JOB_ID_LENGTH));
				sb.append(StringUtil.createFixedLengthString("Name", Constants.JOB_NAME_LENGTH));
				sb.append(StringUtil.createFixedLengthString("Owner", Constants.JOB_OWNER_LENGTH));
				sb.append(StringUtil.createFixedLengthString("Date", Constants.JOB_DATE_LENGTH));
				sb.append(StringUtil.createFixedLengthString("Size", Constants.JOB_SIZE_LENGTH));
				sb.append("\n");

				Iterator iter = queueudPrintJobInfos.iterator();
				// create ASCII string
				while(iter.hasNext()) {
					QueuedPrintJobInfo queuedPrintJobInfo = (QueuedPrintJobInfo) iter.next();
					sb.append(queuedPrintJobInfo.getLongDescription());
					sb.append("\n");
				}
				
				// list print jobs command
				// print job command
				if(0x4 != cmd[0]) {
					throw new LPDException(METHOD_NAME + "cmd[0]=" + cmd[0] + ",should of been 0x4");
				} else {				
					log.debug(METHOD_NAME + "Report Queue State Command Long");
					// write ASCII string to output stream
					log.debug(METHOD_NAME + "Print Queue status long=" + sb.toString());
					os.write(sb.toString().getBytes());
					os.flush();
					// close os connection (handled in LPDCommands.java)
				}
			} catch(QueueException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new LPDException(e);
			} catch(IOException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new LPDException(e);
			}
		} else {
			throw new LPDException(METHOD_NAME + "command not understood, command=" + new String(command));
		}
	}

}