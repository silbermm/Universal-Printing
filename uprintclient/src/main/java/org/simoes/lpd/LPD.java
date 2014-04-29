package org.simoes.lpd;

import java.io.*;
import java.net.*;
import org.apache.log4j.Logger;
import org.simoes.lpd.command.*;
import org.simoes.lpd.common.*;
import org.simoes.lpd.util.*;


/**
 * The Line Printer Daemon (LPD).  Sets up all of the network communication 
 * to run our LPD.  All commands are handled by {@link LPDCommands}. 
 * 
 * @author Chris_Simoes
 *
 */
public class LPD implements Runnable {
	static Logger log = Logger.getLogger(LPD.class);

//	private final static int QUEUE_SIZE = Integer.parseInt(ConfigResources.getProperty("QUEUE_SIZE"));
	private static int COMMAND_PORT = 40515;
	private final static LPD INSTANCE = new LPD(); 
	
	/**
	 * Constructor for LPD.
	 */
	private LPD() {
		super();
		log.debug("LPD(): STARTED");
	}

	/**
	 * This class is a singleton.  
	 * @return the only instance of LPD.
	 */
	public static LPD getInstance(int port) {
            COMMAND_PORT = port;
            return INSTANCE;
	}

	/**
	 * The run method is implemented so this can be run in its own Thread if desired.
	 */
	public void run() {
		final String METHOD_NAME = "run(): ";
		ServerSocket serverSocket = null;
		LPDCommands lpdCommands = new LPDCommands();
		NetUtil netUtil = new NetUtil();
		try {
			serverSocket = new ServerSocket(COMMAND_PORT);
			while(true) {
				log.debug(METHOD_NAME + "trying to accept() socket connection on port " + COMMAND_PORT );
				Socket connection = serverSocket.accept();
				log.debug(METHOD_NAME + "Connection opened.");
				PrintJob job = null;
				log.debug(METHOD_NAME + "Created a new PrintJob.");

				InputStream is = null;
				OutputStream os = null;
				ByteArrayOutputStream baos = null;
				// this reads the command and then closes the socket to prepare for another command                
				try {
					is = connection.getInputStream();                                                                           
					os = connection.getOutputStream();
					//baos = new ByteArrayOutputStream();
                	    
					log.debug(METHOD_NAME + "Got InputStream.");                                        
					byte[] command = netUtil.readCommand(is);

					log.debug(METHOD_NAME + "Command = " + new String(command));

					// pass command on to LPDCommands
					lpdCommands.handleCommand(command, is, os);
				} catch(IOException e) {
					log.debug(METHOD_NAME + "ERROR in try 2");
					log.debug(METHOD_NAME + e.getMessage());
					e.printStackTrace();
				} finally {
					if(null != connection) {
						log.debug(METHOD_NAME + "about to close connection.");
						try { connection.close(); }
						catch(IOException e) {}
					}
					if(null != is) {
						log.debug(METHOD_NAME + "about to close is.");
						try { is.close(); }
						catch(IOException e) {}
					}
					if(null != os) {
						log.debug(METHOD_NAME + "about to close os.");
						try { os.close(); }
						catch(IOException e) {}
					}
				}
				// if the Thread pool has over 100 labels, then stop accepting them
/*
				while(queue.size() > QUEUE_SIZE) {
					//TODO: better to wait on the queue and get notified when it changes
					try {
						Thread.sleep(60000); //sleep for a minute
						log.warn(METHOD_NAME + "Queue has over " + QUEUE_SIZE + " labels, going to sleep.");
					} catch(InterruptedException e) {
						log.error(METHOD_NAME + e.getMessage(), e);
					}
				}
*/				
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(null != serverSocket) {
				try { serverSocket.close(); }
				catch(IOException e) {}
			}
		}
		
	}

	public static void main(String args[]) {
		log.debug("main(): STARTED");
		try {
			LPD lpd = LPD.getInstance(515);
			lpd.run();
		} catch(Exception e) {
			log.fatal(e.getMessage(), e);
		}
		log.debug("main(): FINSHED");
	}
}
