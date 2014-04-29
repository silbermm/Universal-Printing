package org.simoes.util;

import org.apache.log4j.Logger;

/**
 * This is the wrapper object for any Objects passed into our ThreadPool.
 * 
 * @author Chris Simoes
 */
public class ThreadPoolRequest {
    // all files need a static Category object for error logging
    static Logger log = Logger.getLogger(ThreadPoolRequest.class);

	Runnable target;

	ThreadPoolRequest(Runnable t) {
		target = t;
	}
}

