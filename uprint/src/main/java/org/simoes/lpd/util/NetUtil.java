package org.simoes.lpd.util;

import org.simoes.lpd.common.*;

import java.io.*;

import org.apache.log4j.Logger;

/**
 * 
 * Provides utility methods helping us to parse the byte streams
 * sent to us from the client.
 * 
 * @author Chris Simoes
 */
public class NetUtil {
	static Logger log = Logger.getLogger(NetUtil.class);
	
	/**
	 * Default Contructor.
	 *
	 */
	public NetUtil(){
		super();
	}
	
	/**
	 * Reads a command defined by the RFC1179 spec.  Basically 
	 * the command is all the data until we see 0x10.
	 * @param is the InputStream providing us with a command from the client.
	 * @return the command we found in the InputStream
	 * @throws IOException thrown if there is an IO problem
	 */
	public byte[] readCommand(InputStream is) 
		throws IOException
	{
                final String METHOD_NAME = "readCommand(): ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
                	    
		int data = is.read();
		while(-1 != data && 10 != data) {
			baos.write(data);
			data = is.read();
		}
		if(10 == data) {
			baos.write(data); // keep the LineFeed for now
		}

		log.debug(METHOD_NAME + "Command = " + baos.toString());
		
		return baos.toByteArray();
	}

	/**
	 * Used by CommandReceiveJob to read the headers of the 
	 * {@link ControlFile} or the {@link DataFile} from the client.
	 * Please see section 6 of the RFC1179 specification for more details.
	 * @param is the InputStream from the client sending us the print job
	 * @param os the OutputStream to the client we are writing reponses to
	 * @return a byte[] from the InputStream holding the ControlFile or the DataFile 
	 * @throws IOException thrown if there is an IO problem
	 */
	public byte[] readNextInput(InputStream is, OutputStream os) 
		throws IOException
	{
		final String METHOD_NAME = "readNextInput(): ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
                	    
		int data = is.read();
		while(-1 != data && 10 != data) {
			baos.write(data);
			data = is.read();
		}
		if(10 == data) {
			baos.write(data); // keep the LineFeed for now
		}

		log.debug(METHOD_NAME + "Command = " + baos.toString());
		os.write(Constants.ACK);
		
		return baos.toByteArray();
	}

	/**
	 * Reads the {@link ControlFile} and retunrs it as a byte[] 
	 * 
	 * @param is the InputStream from the client sending us the control file
	 * @param os the OutputStream to the client we are writing reponses to
	 * @return a byte[] from the InputStream holding the ControlFile  
	 * @throws IOException thrown if there is an IO problem
	 */
	public byte[] readControlFile(InputStream is, OutputStream os) 
		throws IOException
	{
		final String METHOD_NAME = "readControlFile(): ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
                	    
		int data = is.read();
		while(-1 != data && 0 != data) { //should see a 0
			baos.write(data);
			data = is.read();
		}

		os.write(Constants.ACK);
		
		return baos.toByteArray();
	}

	/**
	 * Reads the {@link DataFile} and returns it as a byte[] 
	 * 
	 * @param is the InputStream from the client sending us the data file
	 * @param os the OutputStream to the client we are writing reponses to
	 * @return a byte[] from the InputStream holding the DataFile  
	 * @throws IOException thrown if there is an IO problem
	 */
	public byte[] readPrintFile(InputStream is, OutputStream os) 
		throws IOException
	{
		final String METHOD_NAME = "readPrintFile(): ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
                	    
		int data = is.read();
		while(-1 != data && 0 != data) { //should see a 0
			baos.write(data);
			data = is.read();
		}

		os.write(Constants.ACK);
		
		return baos.toByteArray();
	}

	/**
	 * Reads the {@link DataFile} and returns it as a byte[] 
	 * 
	 * @param is the InputStream from the client sending us the data file
	 * @param os the OutputStream to the client we are writing reponses to
	 * @param size the number of bytes to read from the stream
	 * @return a byte[] from the InputStream holding the DataFile  
	 * @throws IOException thrown if there is an IO problem
	 */
	public byte[] readPrintFile(InputStream is, OutputStream os, int size) 
		throws IOException
	{
		final String METHOD_NAME = "readPrintFile(): ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int data = is.read();
		int count = 1;                	    
		while(-1 != data && size >= count) { //should see a 0
			baos.write(data);
			data = is.read();
			count++;
		}

		os.write(Constants.ACK);
		
		return baos.toByteArray();
	}

}