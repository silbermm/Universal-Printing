package org.simoes.lpd.command;

import org.simoes.lpd.exception.*;
import org.simoes.lpd.util.*;
import org.simoes.util.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * 
 * This class handles the Remove jobs Command in RFC1179. 
 * The RFC description is below: 
 * <BR>
 * 5.5 05 - Remove jobs<BR>
 * <BR>
 *       +----+-------+----+-------+----+------+----+<BR>
 *       | 05 | Queue | SP | Agent | SP | List | LF |<BR>
 *       +----+-------+----+-------+----+------+----+<BR>
 *       Command code - 5<BR>
 *       Operand 1 - Printer queue name<BR>
 *       Operand 2 - User name making request (the agent)<BR>
 *       Other operands - User names or job numbers<BR>
 * <BR>
 *    This command deletes the print jobs from the specified queue which
 *    are listed as the other operands.  If only the agent is given, the
 *    command is to delete the currently active job.  Unless the agent is
 *    "root", it is not possible to delete a job which is not owned by the
 *    user.  This is also the case for specifying user names instead of
 *    numbers.  That is, agent "root" can delete jobs by user name but no
 *    other agents can.
 * <BR>
 * Operand 1 = Queue name<BR>
 * Operand 2 = User name<BR>
 * Operand 3 = Print Job Number<BR>
 * 
 * NOTE: If user is Administrator or root they can delete any job. 
 * @author Chris Simoes
 *
 */
public class CommandRemovePrintJob extends CommandHandler {
	static Logger log = Logger.getLogger(CommandRemovePrintJob.class);

	public CommandRemovePrintJob(byte[] command, InputStream is, OutputStream os) {
		super(command, is, os);
	}
	
	/**
	 * Removes the print jobs specified by the parameters passed in.  
	 * The queue and user need to be set.  The final parameter needs 
	 * to be a print job number.
	 * @throws LPDException thrown when an error occurs
	 * 
	 */
	public void execute() throws LPDException {
		final String METHOD_NAME = "execute(): ";
		
		Vector info = StringUtil.parseCommand(command);
		if(null != info && info.size() > 1) {
			byte[] cmd = (byte[]) info.get(0);
			byte[] queue = (byte[]) info.get(1);
			byte[] user = (byte[]) info.get(2);
			byte[] jobNumber = new byte[0];
			String queueName = (new String(queue)).trim(); // parseCommand() should guarantee queue is not null
			String userName = (new String(user)).trim();
			String jobId = new String();
			
			try {
				if(info.size() > 2) {
					jobNumber = (byte[]) info.get(3);
					jobId = new String(jobNumber); 
				} else {
					// query queue for last print job if no print job number given
					List queueudPrintJobInfos = Queues.getInstance().listAllPrintJobs(queueName);
					QueuedPrintJobInfo queuedPrintJobInfo = 
						(QueuedPrintJobInfo) queueudPrintJobInfos.get(queueudPrintJobInfos.size()-1); 
					jobId = String.valueOf(queuedPrintJobInfo.getId());
				}

				// remove job command
				if(0x5 == cmd[0]) {
					log.debug(METHOD_NAME + "Remove Print Job Command");
					Queues.getInstance().removePrintJob(queueName, userName, jobId);
					// removes a print job based on the "list" of user names or job numbers passed in
				} else {
					throw new LPDException(METHOD_NAME + "cmd[0]=" + cmd[0] + ",should of been 0x5");
				}
			} catch(QueueException e) {
				log.error(METHOD_NAME + e.getMessage());
				throw new LPDException(e);
			}
		} else {
			throw new LPDException(METHOD_NAME + "command not understood, command=" + new String(command));
		}
	}

}