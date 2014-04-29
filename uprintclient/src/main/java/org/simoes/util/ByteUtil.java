package org.simoes.util;

import org.apache.log4j.Logger;

/**
 * Utility class for manipulating bytes in Java
 * that I have found useful.
 * 
 * @author Chris Simoes
 */
public class ByteUtil {
	static Logger log = Logger.getLogger(ByteUtil.class);
	
	public static byte[] copyByteArray(byte[] a) {
		int size = a.length;
		byte[] copy = new byte[size];
		System.arraycopy(a, 0, copy, 0, size);
		return copy;
	}

}