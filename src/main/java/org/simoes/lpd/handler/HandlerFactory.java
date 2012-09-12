package org.simoes.lpd.handler;

import com.typesafe.config.Config;
import org.apache.log4j.Logger;
import org.simoes.lpd.common.*;
import org.simoes.util.*;
import silbersoft.uprint.ui.PrintView;
import silbersoft.uprint.ui.models.PrintButtonModel;
import silbersoft.uprint.ui.models.PrintViewListModel;
import silbersoft.uprint.service.UCITHandler;

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
        private static PrintButtonModel printButton;
        private static PrintViewListModel buildingList;
        private static PrintView printerView;
        private static Config config;
	
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
	public static HandlerFactory getInstance(Config config, PrintButtonModel printButton, PrintViewListModel buildingList, PrintView printerView) {
                HandlerFactory.config = config;
                HandlerFactory.buildingList = buildingList;
                HandlerFactory.printButton = printButton;
                HandlerFactory.printerView = printerView;
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
                        result = new UCITHandler(config, printButton, buildingList,printerView);
                } else { // if some how it did not get set then default is FILE
			log.error(METHOD_NAME + "The handler is not set.");
			log.error(METHOD_NAME + "Using the default of: " + Constants.VALUE_FILE);
			result = new FileHandler();
		}
		return result;
	}	
}