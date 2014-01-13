package org.simoes.lpd.common;

import org.simoes.util.*;

import org.apache.log4j.Logger;

/**
 * 
 * The dataFile encapsulates the actual print job data that
 * we will send to the printer.
 * @author Chris Simoes
 *
 *
 */
public class DataFile extends PrintFile implements Cloneable {
	static Logger log = Logger.getLogger(DataFile.class);

	public DataFile() {
		super();
	}

	public Object clone() {
		final String METHOD_NAME = "clone()";
		DataFile result = null;
		try {
			result = (DataFile)super.clone();
			result.setContents(ByteUtil.copyByteArray(this.getContents()));
		} catch(CloneNotSupportedException e) {
			log.error(METHOD_NAME + e.getMessage());
			throw new InternalError(METHOD_NAME + e.getMessage());
		}
		return result;
	}
	
}