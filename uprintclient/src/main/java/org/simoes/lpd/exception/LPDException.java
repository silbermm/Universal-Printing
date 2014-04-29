package org.simoes.lpd.exception;

/**
 * The general exception for our LPD project.
 * 
 * @author Chris Simoes
 *
 *
 */
public class LPDException extends Exception {
	public LPDException() {
		super();
	}
	
	public LPDException(String msg) {
		super(msg);
	}
	
	public LPDException(String msg, Throwable t) {
		super(msg,t);
	}

	public LPDException(Throwable t) {
		super(t);
	}
}