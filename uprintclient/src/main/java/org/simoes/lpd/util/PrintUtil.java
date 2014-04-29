package org.simoes.lpd.util;

import org.simoes.lpd.common.*;
import org.simoes.lpd.exception.*;
import org.simoes.util.*;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

//import org.simoes.lpd.common.Constants;

/**
 * This is a utility class to help us print labels.
 */
public class PrintUtil {
	static Logger log = Logger.getLogger(PrintUtil.class);
	static final String CMD;
	static final String TEMP_PDF = "temp.pdf";

	static {
		String osName = System.getProperty("os.name" );
		log.debug("PrintUtil[static]: " + "os.name=" + osName);
//		 FOR WINDOWS 95 AND 98 USE COMMAND.COM
		if( osName.equals( "Windows 95" ) || osName.equals( "Windows 98" )){
		   CMD = "command.com  /C start ";
		}
//		 FOR WINDOWS NT/XP/2000 USE CMD.EXE
		else {
			CMD = "cmd /C ";
		}
	}
	

	/**
	 * Executes command "cmd /c AcroRd32.exe /t c:\some\file\name.txt printerName".
	 * This method blocks until the command completes.
	 * 
	 * @param filename - A File representing the pdf we want to print.
	 * @param printer - Printer to send PDF to, if this is null use default printer
	 */
	public void printPDF(File filename, String printer) throws LPDException {
		final String METHOD_NAME = "printPDF(): ";
		final String ACROBAT = ConfigResources.getProperty(Constants.ACROREAD_PROGRAM);
		if(!filename.exists()) {
			throw new LPDException(METHOD_NAME + filename.getAbsolutePath() + " does not exist.");
		} else if(StringUtil.isEmpty(printer)) {
			log.info(METHOD_NAME + "Printer string was empty.");
			log.info(METHOD_NAME + "Using default printer.");
			printer = "";
		}
		
		//String command = CMD + "\"" + ACROBAT + "\"" + " /t " + filename.getAbsolutePath() + " " + printer;
		String command = CMD + "\"" + ACROBAT + "\"" + " /p /h " + filename.getAbsolutePath() + " " + printer;
		log.debug(METHOD_NAME + "About to try:" + command);
		Runtime runtime = Runtime.getRuntime();
		try {
			Process pid = runtime.exec(command);
			int returnValue = pid.waitFor();
			if(0 == returnValue) {
				log.debug(METHOD_NAME + "Process successfully completed.");
			} else {
				log.warn(METHOD_NAME + "Process terminated abnomally, with an exit code of: " + returnValue);
			}
		} catch(InterruptedException e) {
			log.error(METHOD_NAME + e.getMessage(), e);
			throw new LPDException(METHOD_NAME + e.getMessage());
		} catch(IOException e) {
			log.error(METHOD_NAME + "Tried to exec " + command + " , but we failed.");
			log.error(METHOD_NAME + e.getMessage(), e);
			throw new LPDException(METHOD_NAME + e.getMessage());
		}
	}

	/**
	 * This version takes the byte[] passed in and creates a temporary file.
	 * It then calls the printPDF() above
	 */
	public void printPDF(byte[] data, String printer) throws LPDException {
		final String METHOD_NAME = "printPDF(): ";
		String tempFilename = ConfigResources.getProperty(Constants.TEMP_DIR) + TEMP_PDF;
		try {
			File tempFile = FileUtil.writeFile(data, tempFilename);
			printPDF(tempFile, printer);
			if(!tempFile.delete()) {
				log.warn(METHOD_NAME + "Could not delete: " + tempFile.getAbsolutePath());
			}
		} catch(IOException e) {
			log.error(METHOD_NAME + e.getMessage(), e);
		}
	}

}