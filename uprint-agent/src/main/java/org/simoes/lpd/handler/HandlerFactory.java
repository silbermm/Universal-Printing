package org.simoes.lpd.handler;

import akka.actor.ActorSystem;
import co.silbersoft.uprint.service.UCITHandler;

import com.typesafe.config.Config;

import org.apache.log4j.Logger;
import org.simoes.lpd.common.*;
import org.simoes.util.*;

/**
 * Creates the concrete implementation of the Handler we
 * will use to process PrintJobs based on the 
 * properties file.  The default if there are problems is FILE. 
 * 
 * @author Chris Simoes
 */
public class HandlerFactory {
	static Logger log = Logger.getLogger(HandlerFactory.class);

	private final static HandlerFactory INSTANCE = new HandlerFactory();
	private String handler;
    private static Config config;
	private static ActorSystem actorSystem;
    
	/**
	 * Constructor for LPD.
	 */
	private HandlerFactory() {
		super();
		final String METHOD_NAME = "HandlerFactory(): ";
		log.debug(METHOD_NAME + "STARTED");
		handler = "UCIT";
		if(StringUtil.isEmpty(handler)) {
			log.warn(METHOD_NAME + "The handler was not set from the properties file.");
			log.warn(METHOD_NAME + "Using the default of: " + Constants.VALUE_FILE);
			handler = Constants.VALUE_FILE;
		}
	}

	/**
	 * This class is a singleton.  
	 * @return the only instance of HandlerFactory
	 */
	public static HandlerFactory getInstance(Config config, ActorSystem actorSystem) {
                HandlerFactory.config = config; 
                HandlerFactory.actorSystem = actorSystem;
		return INSTANCE;
	}
	
	/**
	 * Returns the concrete implementation of the HandlerInterface based
	 * on the settings in the properties file.
	 * @return HandlerInterface the concrete implementation of the HandlerInterface
	 */
	public HandlerInterface getPrintHandler() {
		final String METHOD_NAME = "getPrintHandler(): ";
		HandlerInterface result = null;
		if(Constants.VALUE_PRINTER.equals(handler.trim())) {
			result = new PDFPrintHandler();
		} else if(Constants.VALUE_FILE.equals(handler.trim())) {
			result = new FileHandler();
		} else if(Constants.VALUE_NETWORK.equals(handler.trim())) {
			log.error(METHOD_NAME + "No Handler implemented yet for:" + Constants.VALUE_NETWORK);
		} else if(Constants.VALUE_DATABASE.equals(handler.trim())) {
			log.error(METHOD_NAME + "No Handler implemented yet for:" + Constants.VALUE_DATABASE);
                } else if("UCIT".equals(handler.trim())) {
                        log.debug(METHOD_NAME + "Chose the UCIT Handler");
                        result = new UCITHandler(config, actorSystem);
                } else { // if some how it did not get set then default is FILE
			log.error(METHOD_NAME + "The handler is not set.");
			log.error(METHOD_NAME + "Using the default of: " + Constants.VALUE_FILE);
			result = new FileHandler();
		}
		return result;
	}	
}