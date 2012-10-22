package org.simoes.util;

import java.text.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * 
 * Utility class for manipulating Dates in Java
 * that I have found useful.
 * 
 * @author Chris Simoes
 */
public class DateUtil {
	static Logger log = Logger.getLogger(DateUtil.class);
    
	public static java.sql.Date utilDate2SQLDate(java.util.Date d) {
		java.sql.Date result = null;
		if(null != d) {
			result = new java.sql.Date(d.getTime());
		}
		return result;
	}

	public static java.sql.Timestamp utilDate2Timestamp(java.util.Date d) {
		java.sql.Timestamp result = null;
		if(null != d) {
			result = new java.sql.Timestamp(d.getTime());
		}
		return result;
	}

	/**
	 * Creates a String that represents the current Date and Time.
	 */ 
	public static String createDateString() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		return df.format(new Date());
	}

	/**
	 * Creates a String that represents the Date passed in.
	 * If the date passed in is null the null is returned.
	 */ 
	public static String createDateString(Date d) {
		String result = null;
		if(null != d) {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			result = df.format(d);
		}
		return result;
	}

	/**
	 * Creates a String formatted properly based on the Date passed in.
	 * Is expecting a string of the following form: "year-month-day hour:minute:second"
	 * This is the date format returned from MSSQL server via JDBC
	 * Note: hour is from 0-23
	 * If the date passed in is null the null is returned.
	 */ 
	public static String createDateString(String d) {
		final String METHOD_NAME = "createDateString(): ";
		String result = null;
		if(!StringUtil.isEmpty(d)) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date myDate = df.parse(d);
				result = createDateString(myDate);
			} catch(ParseException e) {
				log.error(METHOD_NAME + "String = " + d+ ", was not a valid date.");
			}
		}
		return result;
	}

	/**
	 * Creates a String that represents the Date passed in, using the format
	 * passed in.  For formatting options check out the SimpleDateFormat object.
	 * If the date passed in is null the null is returned.
	 */ 
	public static String createDateOnlyString(Date d) {
		String result = null;
		if(null != d) {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			result = df.format(d);
		}
		return result;
	}
    
	/**
	 * Creates a String that represents the Date passed in.
	 * Formats the Date so that it can be used as a filename.  
	 * For formatting options check out the SimpleDateFormat object.
	 * If the date passed in is null the null is returned.
	 */ 
	public static String createDateOnlyFilename(Date d) {
		String result = null;
		if(null != d) {
			SimpleDateFormat df = new SimpleDateFormat("MM_dd_yyyy");
			result = df.format(d);
		}
		return result;
	}

	public static Calendar createCalendar(java.util.Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * Returns a true or false based on
	 * the whether or not the date is valid.
	 */
	public static boolean checkDate(int day, int month, int year) {
		int[] numDays = {31,28,31,30,31,30,31,31,30,31,30,31};

		//if february
		if(month == 1) {
			// if leap year
			if((((year % 4) == 0) && ((year % 100) != 0))
			   || (((year % 100) == 0) && ((year % 400) == 0))) {
				return(day <= 29);
			} else {
				return(day <= 28);
			}
		} else {
			// if day is greater than possible days of the month passed
			return(day <= numDays[month]);
		}
	} // END checkDate

}
