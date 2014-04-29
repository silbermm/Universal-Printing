package org.simoes.lpd.exception;

/**
 * Shows when an exceptional condition has occurred 
 * in one of our Queues.
 * 
 * @author Jason Crowe
 *
 */
public class QueueException extends Exception {
	public QueueException() {
		super();
	}
	
	public QueueException(String msg) {
		super(msg);
	}
	
	public QueueException(String msg, Throwable t) {
		super(msg,t);
	}

	public QueueException(Throwable t) {
		super(t);
	}
}