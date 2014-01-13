package org.simoes.util;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * The Thread Pool will execute any threads passed in in a FIFO order.
 * Available Thread will pick up any incoming requests.
 * Any requests when all Threads are busy will be handled as soon as possible.
 * 
 * @author Chris Simoes
 */
public class ThreadPool {
    // all files need a static Category object for error logging
	static Logger log = Logger.getLogger(ThreadPool.class);

	Vector objects; 
	ThreadPoolThread[] poolThreads;
	boolean terminated = false;

	public ThreadPool(int n) {
		objects = new Vector();
		poolThreads = new ThreadPoolThread[n];
		for (int i = 0; i < n; i++) {
			poolThreads[i] = new ThreadPoolThread(this, i);
			poolThreads[i].start();
		}
	}

    /**
     * Use this to add to the Thread Pool.
     */
	public void add(Runnable target) {
			if (terminated)  {
				throw new IllegalStateException("Thread pool has shutdown");
			}
			synchronized(objects)  {
			   objects.addElement(new ThreadPoolRequest(target));
			   objects.notify();
			}
	}

    /**
     * Waits until all Threads have terminated and then it kills the ThreadPool.
     */
	void waitForAll(boolean terminate)  {
			if (terminate) {
				for (int i = 0; i < poolThreads.length; i++)  {
				   poolThreads[i].setEventsStillFiring(false);
				}
				terminated = true;
			}
	}

	/**
	 * Closes the ThreadPool.  
	 */
	public void closePool()  {
		waitForAll(false);
	}
	
	/**
	 * Returns the number of objects waiting for a thread.
	 */
	public int getQueueSize() {
	    return objects.size();
	}
}
