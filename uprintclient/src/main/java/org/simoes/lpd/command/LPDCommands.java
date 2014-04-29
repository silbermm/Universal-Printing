package org.simoes.lpd.command;

import org.simoes.lpd.exception.*;
import org.simoes.util.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * Creates the concrete command classes to process incoming commands.
 * Then it calls the execute() method on them.
 *  
 * @author Chris Simoes
 *
 */
public class LPDCommands {
	static Logger log = Logger.getLogger(LPDCommands.class);

	public LPDCommands() {
		super();
	}

	/**
	 * Creates the concrete instance of the command class required.
	 * Then if calls that class' execute() method.
	 * @param command byte[] passed in from the client
	 * @param is InputStream from the client
	 * @param os OutputStream to the client
	 */
	public void handleCommand(byte[] command, InputStream is, OutputStream os) 
	{
		final String METHOD_NAME = "handleCommand(): ";

		CommandHandler commandHandler = null;
		try{
			commandHandler = createCommandHandler(command, is, os);
			commandHandler.execute();
		} catch(LPDException e) {
			log.error(METHOD_NAME + "Could not properly handle command:" + command);
			log.error(METHOD_NAME + e.getMessage());
		}
	}

	private CommandHandler createCommandHandler(byte[] command, InputStream is, OutputStream os) 
		throws LPDException
	{
		final String METHOD_NAME = "createCommandHandler(): ";

		CommandHandler result = null;
		Vector info = StringUtil.parseCommand(command);
		try {
			if(null != info && info.size() > 0) {
				byte[] cmd = (byte[]) info.get(0);
				// receive job command
				if(0x1 == cmd[0]) {
					log.debug(METHOD_NAME + "Print Job Command");
					result = new CommandPrintJob(command, is, os);
				} else if(0x2 == cmd[0]) {
					log.debug(METHOD_NAME + "Receive Job Command");
					result = new CommandReceiveJob(command, is, os);
				} else if(0x3 == cmd[0]) {
					log.debug(METHOD_NAME + "Report Queue State Short Command");
					result = new CommandReportQueueStateShort(command, is, os);
				} else if(0x4 == cmd[0]) {
					log.debug(METHOD_NAME + "Report Queue State Long Command");
					result = new CommandReportQueueStateLong(command, is, os);
				} else if(0x5 == cmd[0]) {
					log.debug(METHOD_NAME + "Remove Print Job Command");
					result = new CommandRemovePrintJob(command, is, os);
				} else {
					throw new LPDException(METHOD_NAME + "We do not support command:" + cmd);
				}
			} else {
				throw new LPDException(METHOD_NAME + "command passed in was bad, command=" + command);
			}
		} catch(LPDException e) {
			log.error(METHOD_NAME + "Could not properly handle command:" + command);
			log.error(METHOD_NAME + e.getMessage());
		}
		return result;
	}

}