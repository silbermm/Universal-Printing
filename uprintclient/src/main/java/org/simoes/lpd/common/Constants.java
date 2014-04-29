package org.simoes.lpd.common;

/**
 * 
 * Holds all of our static Constants in our project.
 * @author Chris Simoes
 *
 */
public class Constants {
												
	public final static byte[] ACK = {0x0};
	
	public final static String MENU_ABOUT = "About";
	public final static String MENU_EDIT = "Edit";

	public final static String MENU_ITEM_DELETE_ALL = "Delete All";
	public final static String MENU_ITEM_ABOUT = "About";

	public final static String USER_ROOT = "root";
	public final static String USER_ADMINISTRATOR = "Administrator";

	public final static int JOB_ID_LENGTH = 8;
	public final static int JOB_NAME_LENGTH = 20;
	public final static int JOB_OWNER_LENGTH = 15;
	public final static int JOB_DATE_LENGTH = 20;
	public final static int JOB_SIZE_LENGTH = 9;
	
	// Property Keys from lpd.properties
	public final static String ACROREAD_PROGRAM = "ACROREAD_PROGRAM";
	public final static String KEY_PRINT_JOB_HANDLER = "PRINT_JOB_HANDLER";
	public final static String TEMP_DIR = "TEMP_DIR";

	// Property Values from lpd.properties
	public final static String VALUE_PRINTER = "PRINTER";
	public final static String VALUE_FILE = "FILE";
	public final static String VALUE_NETWORK = "NETWORK";
	public final static String VALUE_DATABASE = "DATABASE";
	
}