package org.simoes.lpd.command;

import org.simoes.lpd.exception.*;
import org.simoes.util.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * 
 * This class handles the Print any waiting jobs Command in RFC1179. 
 * The RFC description is below:
 * <BR>
 *  5.1 01 - Print any waiting jobs<BR>
 *  <BR>
 *       +----+-------+----+<BR>
 *       | 01 | Queue | LF |<BR>
 *       +----+-------+----+<BR>
 *       Command code - 1<BR>
 *       Operand - Printer queue name<BR>
 * <BR>
 *    This command starts the printing process if it not already running.
 * NOTE: This is not implemented since we intend to hold the print job
 * 
 *  @author Chris Simoes
 */
public class CommandPrintJob extends CommandHandler {
	static Logger log = Logger.getLogger(CommandPrintJob.class);

	public CommandPrintJob(byte[] command, InputStream is, OutputStream os) {
		super(command, is, os);
	}
	
	/**
	 * Currently does nothing.  
	 * @throws LPDException thrown when an error occurs
	 */
	public void execute() throws LPDException {
		final String METHOD_NAME = "execute(): ";
		
		Vector info = StringUtil.parseCommand(command);
		if(null != info && info.size() > 0) {
			byte[] cmd = (byte[]) info.get(0);
			// print job command
			if(0x1 == cmd[0]) {
				log.debug(METHOD_NAME + "Print Job Command");

			} else {
				throw new LPDException(METHOD_NAME + "cmd[0]=" + cmd[0] + ",should of been 0x1");
			}
		} else {
			throw new LPDException(METHOD_NAME + "command not understood, command=" + new String(command));
		}
	}


}