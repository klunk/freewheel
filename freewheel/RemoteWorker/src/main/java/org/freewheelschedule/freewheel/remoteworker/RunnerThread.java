package org.freewheelschedule.freewheel.remoteworker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;

public class RunnerThread implements Runnable, Runner {

	private static Log log = LogFactory.getLog(RunnerThread.class);
	
	private @Setter int numberOfThreads;
	private @Setter BlockingQueue<JobInitiationMessage> jobQueue;
	private ExecutorService threadPool;
	
	@Override
	public void run() {
		log.info("Worker thread is running.");
	
		threadPool = Executors.newFixedThreadPool(numberOfThreads);
		
		while(true) {
			try {
				log.debug("Waiting for command to come from listener.");
				JobInitiationMessage command = jobQueue.take();
				log.info("Command received: " + command);
				Execution commandLine = new CommandLineExecution();
				if (commandLine instanceof CommandLineExecution) {
					((CommandLineExecution) commandLine).setCommand(command.getCommand());
				}
				threadPool.submit(commandLine);
			} catch (InterruptedException e) { 
				log.error("RunnerThread sleep was interrupted", e);
			}
		}

	}
	
	public void stopExecutions() {
		threadPool.shutdownNow();
	}

}
