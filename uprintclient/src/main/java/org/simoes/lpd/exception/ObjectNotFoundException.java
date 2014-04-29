package org.simoes.lpd.exception;

/**
 * When an expected object is not found this is thrown.
 * Used by our {@link org.simoes.lpd.util.Queue}. 
 * 
 * @author Jason Crowe
 *
 *
 */
public class ObjectNotFoundException extends Exception {
// 	public LPDException() {
// 		super();
// 	}
	
	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
// 	public LPDException(String msg, Throwable t) {
// 		super(msg,t);
// 	}

// 	public LPDException(Throwable t) {
// 		super(t);
// 	}
}
