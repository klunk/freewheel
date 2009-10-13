package org.freewheelschedule.freewheel.remoteworker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ListenerThread implements Listener, Runnable {

	private static Log log = LogFactory.getLog(ListenerThread.class);
	
	public void run() {
		
		log.info("Freewheel RemoteWorker listening ...");
		while(true) {
			log.debug("In the Listener thread");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				log.error("Sleep interrupted", e);
			}
		}

	}

}
