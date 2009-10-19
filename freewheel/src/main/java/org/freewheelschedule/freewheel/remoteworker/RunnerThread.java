package org.freewheelschedule.freewheel.remoteworker;

import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import lombok.Setter;

public class RunnerThread implements Runnable, Runner {

	private static Log log = LogFactory.getLog(RunnerThread.class);
	
	private @Setter int numberOfThreads;
	private @Setter Queue<String> jobQueue;
	
	@Override
	public void run() {
		log.info("Worker thread is running.");
		
		while(true) {
			log.debug("Waiting for command to come from listener.");
			String command = jobQueue.poll();
			if (command != null) {
				log.info("Command received: " + command);
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error("RunnerThread sleep was interrupted", e);
				}
			}
		}

	}

}
