/**
 * This class is used to start the RemoteWorker service. It contains
 * just a main method which starts a new thread process and a shutdown 
 * hook which will be used to stop the server process and all its threads
 *
 * @author wiehsim
 *
 */
package org.freewheelschedule.freewheel.remoteworker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RemoteWorker {

	private static Log log = LogFactory.getLog(RemoteWorker.class);
	/**
	 * main method to start the RemoteWorker processes.
	 * @param args
	 */
	public static void main(String[] args) {
		
		log.info("Freewheel RemoteWroker running ....");
		Runnable runner = new ListenerThread();

		Thread thread = new Thread(runner);
		
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			log.error("Join interrupted", e);
		}

	}

}
