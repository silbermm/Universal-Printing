package org.simoes.util;

import java.io.*;
//import java.util.*;

import org.apache.log4j.Logger;

/**
 * 
 * Utility class for manipulating Files in Java
 * that I have found useful.
 * 
 * @author Chris Simoes
 */
public class FileUtil
{
	// error logging variables //
	static Logger log = Logger.getLogger(FileUtil.class);
	private static final String METHOD_writeFile = "writeFile(): ";
	private static final String METHOD_deleteFiles = "deleteFiles(): ";
	private static final String METHOD_runProgram = "runProgram(): ";
	private static final String METHOD_readInputStream = "readInputStream(): ";
	private static final String STARTED = "started.";
	private static final String FINISHED = "finished.";
	private static final String ERROR = "**ERROR** ";
	// end of error logging variables //
    
	/**
	 *  Writes the entire contents of the InputStream to a file named the
	 *  filename passed in.
	 * @param inputStream - the inputStream to be written to file
	 * @param filename - the file that the inputStream will be written to
	 * @return File - a File object representing the file we just created, or
	 * null if there was an error.
	 */
	public static File writeFile(InputStream inputStream, String filename)
		throws IOException
	{
		log.debug(METHOD_writeFile + STARTED);
		File result = null;
		int byteCount = -1;
		byte[] data = new byte[1024];
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
		while ((byteCount = bis.read(data, 0, 1024)) > -1)  {
			bos.write(data, 0, byteCount);
		}
		bos.flush();

		if(null != bos)  {
			try { bos.close(); }
			catch(IOException e) {}
		}
		if(null != bis)  {
			try { bis.close(); }
			catch(IOException e) {}
		}
		result = new File(filename);
		return result;
	}

	/**
	 *  Writes the String data to a file named filename.
	 * @param data - A String that will be written to file
	 * @param filename - the file that the inputStream will be written to
	 * @return File - a File object representing the file we just created, or
	 * null if there was an error.
	 */
	public static File writeFile(String data, String filename)
		throws IOException
	{
		File result = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());
		result = writeFile(bais, filename);
		if(null != bais) {
			try { bais.close(); }
			catch(IOException e) {}
		}
		return result;
	}

	/**
	 * Writes the byte[] data to a file named filename.
	 * @param data - A byte[] that will be written to file
	 * @param filename - the file that the inputStream will be written to
	 * @return File - a File object representing the file we just created, or
	 * null if there was an error.
	 */
	public static File writeFile(byte[] data, String filename)
		throws IOException
	{
		File result = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		result = writeFile(bais, filename);
		if(null != bais) {
			try { bais.close(); }
			catch(IOException e) {}
		}
		return result;
	}

	/**
	 *  Deletes the files passed in the String array.  Basically it calls
	 * the delete method in the File object.
	 * @param filenames - the files to be deleted
	 */
	public static void deleteFiles(String[] filenames)
	{
		for(int i=0; i<filenames.length; i++)
		{
			deleteFile(filenames[i]);
		}
	}

	/**
	 *  Deletes the file passed in.  Basically it calls
	 * the delete method in the File object.
	 * @param filename - the file to be deleted
	 */
	public static void deleteFile(String filename)
	{
		File file = new File(filename);
		try
		{
			if(file.delete())  {
				log.debug(METHOD_deleteFiles + filename + " was successfully deleted");
			}
			else  {
				log.error(METHOD_deleteFiles + "Error deleting \"" + filename + "\"");
			}
		}
		catch(SecurityException e)
		{
			log.error(METHOD_deleteFiles + "Error deleting \"" + filename + "\"");
			log.error(METHOD_deleteFiles + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 *  Deletes the file passed in.  Basically it calls
	 * the delete method in the File object.
	 * @param filename - the file to be deleted
	 */
	public static void deleteFile(File filename)
	{
		try
		{
			if(filename.delete())  {
				log.debug(METHOD_deleteFiles + filename + " was successfully deleted");
			}
			else  {
				log.error(METHOD_deleteFiles + "Error deleting \"" + filename + "\"");
			}
		}
		catch(SecurityException e)
		{
			log.error(METHOD_deleteFiles + "Error deleting \"" + filename + "\"");
			log.error(METHOD_deleteFiles + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 *  Runs the program passed in.  It the returns the "return code"
	 *  of the program.  0 usually means normal termination.  The
	 *  System.out of the program is stored in the ByteArrayOutputStream
	 *  that is passsed in.
	 */
	public static int runProgram(String program, ByteArrayOutputStream baos)
	{
		int result = -1;
		InputStream is = null;
		try
		{
			Process proc = Runtime.getRuntime().exec(program);
			is = proc.getInputStream();
			readInputStream(is, baos);
			result = proc.waitFor();
		}
		catch(InterruptedException e)
		{
			log.error(METHOD_runProgram + e.getMessage());
			e.printStackTrace();
		}
		catch(IOException e)
		{
			log.error(METHOD_runProgram + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(null != is)  {
				try  {  is.close(); }
				catch(IOException e) {} // we tried
			}
		}
		return result;
	}

	/**
	 *  Converts the InputStream to the returned String.
	 */
	public static String inputStream2String(InputStream is)
	{
		String result = new String();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		readInputStream(is, baos);
		result = baos.toString();
		if(null != baos)  {
			try { baos.close(); }
			catch(IOException e) {}
		}
		return result;
	}

	/**
	 *  Reads the InputStream into the ByteArrayOutputStream.
	 *  Wrapper that has a more descriptive name than readInputStream.
	 */
	public static ByteArrayOutputStream inputStream2ByteArray(InputStream is)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		readInputStream(is, baos);
		return baos;
	}

	/**
	 *  Reads the InputStream into the ByteArrayOutputStream.
	 */
	public static void readInputStream(InputStream is, ByteArrayOutputStream baos)
	{
		int data = -1;
		try
		{
			while(-1 != (data = is.read()))
			{
				baos.write(data);
			}
		}
		catch(IOException e)
		{
			log.error(METHOD_readInputStream + e.getMessage());
			e.printStackTrace();
		}
	}

}