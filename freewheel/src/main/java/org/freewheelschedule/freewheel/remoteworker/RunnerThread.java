package org.freewheelschedule.freewheel.remoteworker;

import java.util.concurrent.BlockingQueue;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RunnerThread implements Runnable, Runner {

	private static Log log = LogFactory.getLog(RunnerThread.class);
	
	private @Setter int numberOfThreads;
	private @Setter BlockingQueue<String> jobQueue;
	
	@Override
	public void run() {
		log.info("Worker thread is running.");
		
		while(true) {
			try {
				log.debug("Waiting for command to come from listener.");
				String command = jobQueue.take();
				log.info("Command received: " + command);
			} catch (InterruptedException e) {
				log.error("RunnerThread sleep was interrupted", e);
			}
		}

	}

}
