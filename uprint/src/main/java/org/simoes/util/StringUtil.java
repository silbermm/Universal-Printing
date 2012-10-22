package org.simoes.util;

//import com.dell.eng.common.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * Utility class for manipulating Strings in Java
 * that I have found useful.
 * 
 * @author Chris Simoes
 */
public class StringUtil {
	static Logger log = Logger.getLogger(StringUtil.class);
	
	/**
	 * Creates a Vector of byte[] from the command passed in.
	 * @param command the bytes read in from the lpr client
	 * @return Vector Created by parsing on white space the 
	 * 		command byte[] passed in
	 */
	public static Vector parseCommand(byte[] command) {
		final String METHOD_NAME = "parseCommand(): ";
		Vector result = new Vector();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// if command is empty return empty Vector
		if(null != command && command.length > 0) {
			// get the command code (the first byte)
			byte[] commandCode = new byte[1];
			commandCode[0] = command[0];
			result.add(commandCode);
			
			for(int i=1; i<command.length; i++) {
				// if we see a space or a line feed 
				// then add the bytes acquired to the Vector
				if(32 == command[i] || 10 == command[i]) {
					result.add(baos.toByteArray());
					baos = new ByteArrayOutputStream();
				} else {
					baos.write(command[i]);
				}
			}
		} 
		return result;
		
	}
	
	/**
	 * This parses the "name" field passed in the header of the control file
	 * or the data files.
	 * Example: Control File header = cfa001MyComputer
	 * Example: Data File header = dfa001MyComputer
	 */
	public static Vector parsePrintFileName(String header) {
		final String METHOD_NAME = "parsePrintFileName(): ";
		Vector result = new Vector();
		String first3Chars = header.substring(0,3);
		String jobNumber = header.substring(3,6);
		String hostName = header.substring(6);
		result.add(first3Chars);
		result.add(jobNumber);
		result.add(hostName);
		return result;		
	}

	/**
	 * Replaces the "replace" String with the "replaceWith" String in the "line" String.
	 * All occurances are replaced.
	 * If "replace" can't be found in "line" then the String "line" is returned.
	 * If "replace" or "replaceWith" are null then "line" is returned.
	 * @param line - String operated on
	 * @param replace - text we will be replacing
	 * @param replaceWith - text that will be substituted for "replace" text.
	 * @return String - the result of the replace
	 */
	public static String replaceString(String line, String replace, String replaceWith)
	{
		final String METHOD_NAME = "replaceString(): ";
		StringBuffer result = new StringBuffer();
		// check for nulls
		if(null == line) {
			return "";
		} else if(null == replace || null == replaceWith) {
			return line;
		}

		int startOfReplace = line.indexOf(replace);
		int endOfReplace = startOfReplace + replace.length();
		int currentPos = 0;
        
		if(-1 == startOfReplace) {
			log.debug(METHOD_NAME + "Replace string(" + replace + ") not found in the line passed in.");
			return line;
		}
		while(-1 != startOfReplace) {
			result.append(line.substring(currentPos, startOfReplace));
			currentPos = endOfReplace;
			result.append(replaceWith);
			startOfReplace = line.indexOf(replace, currentPos);
			endOfReplace = startOfReplace + replace.length();
		} 
		result.append(line.substring(currentPos));
		return result.toString();
	}
    
	/**
	 * Converts the Map passed in to a string containing "key=value" pairs
	 * separated by \n characters.
	 */
	public static String map2String(Map map) {
		final String METHOD_NAME = "map2String(): ";

		StringBuffer sb = new StringBuffer();
		Object key = null;
		Object value = null;
        
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext()) {
			key = iter.next();
			value = map.get(key);
			sb.append(key.toString());
			sb.append("=");
			sb.append(value.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
    
	/**
	 * Returns true if the String data contains any of the 
	 * Strings contained in the ArrayList.
	 * @param data - A string that we will search
	 * @param matches - An ArrayList that contains only Strings.  We will search
	 * data to see if any of the Strings in matches are found in data.
	 * @return String - the first string in matches that was found in data, 
	 * or null if no match was found.
	 */
	public static String containsAnyStrings(String data, ArrayList matches) {
		final String METHOD_NAME = "containsAnyStrings(): ";

		if(null == data || null == matches) {
			return null;
		}
        
		Iterator iter = matches.iterator();
		while(iter.hasNext()) {
			String test = (String) iter.next();
			if(-1 != data.indexOf(test)) {
				return test;
			}
		}
		return null;
	}

	/**
	 * Returns true if the String data exactly matches one of the strings in 
	 * contained in the ArrayList matches. 
	 * @param data - A string that we will search
	 * @param matches - An ArrayList that contains only Strings.  We will search
	 * data to see if any of the Strings in matches are found in data.
	 * @param ignoreCase - true = ignore case when comparing
	 * @return String - the first string in matches that was found in data, 
	 * or null if no match was found.
	 */
	public static String containsOnlyString(String data, ArrayList matches, boolean ignoreCase) {
		final String METHOD_NAME = "containsOnlyString(): ";

		if(null == data || null == matches) {
			return null;
		}
        
		Iterator iter = matches.iterator();
		while(iter.hasNext()) {
			String test = (String) iter.next();
			if(ignoreCase) {
				if(data.equalsIgnoreCase(test)) {
					return test;
				}
			} else {
				if(data.equals(test)) {
					return test;
				}
			}
		}
		return null;
	}
    
	/**
	 * Returns an empty string "" if str is null.  If str is not null
	 * then str is returned.
	 */
	public static String null2Empty(String str) {
		String result = "";
		if(null != str) {
			result = str;
		}
		return result;
	}

	public static boolean isEmpty(String str) {
		boolean result = true;
		if(null != str && str.length() > 0) {
			result = false;
		}
		return result;
	}
    
	public static String checkYOrN(String str) {
		final String METHOD_NAME = "checkYOrN(): ";
		String result = null;
		if(null == str) {
			result = null;
		} else if(str.equalsIgnoreCase("Y")) {
			result = "Y";
		} else if(str.equalsIgnoreCase("N")) {
			result = "N";
		} else {
			log.error(METHOD_NAME + "String was not null, nor was it a Y or an N.");
			log.error(METHOD_NAME + "String passed in:" + str);
		}
		return result;
	}
    
	/**
	 * Creates a URL from the passed in parameters. It has
	 * the following look:<BR>
	 * webpage?param1=value1&param2=value2......
	 * @param webpage - the destination we are headed to
	 * @param params - the parameters we append to the Destination, note the order
	 * of these parameters is random
	 * @return String - the constructed Destination
	 */
	public static String constructURL(String webpage, Properties params) {
		final String METHOD_NAME = "constructURL(): ";
		StringBuffer sb = new StringBuffer();
		sb.append(webpage);
		sb.append("?");
		Iterator iter = params.keySet().iterator();
		while(iter.hasNext()) {
			String key = (String) iter.next();
			String value = (String) params.get(key);
			sb.append(key);
			sb.append("=");
			sb.append(value);
			if(iter.hasNext()) {
				sb.append("&");
			}
		}
		return sb.toString();
	}
 
	/**
	 * Creates a Vector of lines parsed on newline characters.
	 */
	public static Vector getLines(String multiLine) {
		Vector result = new Vector();
        
		StringTokenizer st = new StringTokenizer(multiLine, "\n");
		while(st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result;
	}
	
	/**
	 * Constructs a String of the fixed length given.
	 * If the string passed in is greater than length, 
	 * then the string is truncated to length.  
	 * If the string is greater than length then the string 
	 * is padded with spaces.
	 * If s is empty or null, then an empty string of spaces is returned.
     * @param s - string to operate on
     * @param length - desired length
     * @return - fixed length String
	 */
	public static String createFixedLengthString(String s, int length) {
		String result = null;
		if(StringUtil.isEmpty(s)) {
			StringBuffer sb = new StringBuffer(length);
			for(int i=0; i<length; i++) {
				sb.append(" ");
			}
			result = sb.toString();
		} else {
			if(s.length() == length) {
				result = s;
			} else if(s.length() > length) {
				result = s.substring(0, length);
			} else { // s.length() is < length
				StringBuffer sb = new StringBuffer(length);
				sb.append(s);
				for(int i=s.length(); i<length; i++) {
					sb.append(" ");
				}
				result = sb.toString();
			}
		}
		return result; 
	}
 
 	public static void main(String args[]) {
		byte[] testCommand = {0x2, 0x35, 0x35, 0x35, 0x35, 0x20, 0x36, 0x36, 0x36, 0x36, 0xA };
		Vector commands = StringUtil.parseCommand(testCommand);
		System.out.println("Vector size=" + commands.size());
		
	}
}