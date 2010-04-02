/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.remoteworker;

import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RunnerThread implements Runnable, Runner, InitializingBean {

	private static Log log = LogFactory.getLog(RunnerThread.class);
	
	private @Setter int numberOfThreads;
	private @Setter BlockingQueue<JobInitiationMessage> jobQueue;
	private @Setter ExecutorService threadPool;
	private @Setter boolean continueWaiting = true;
	private @Setter int timeout;
	
	public RunnerThread() {
		super();
	}
	
	public RunnerThread(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
		createThreadPool();
	}
	
	@Override
	public void run() {
		log.info("Worker thread is running.");
	
		Execution commandLine = null;
		
		do {
			try {
				log.debug("Waiting for command to come from listener.");
				JobInitiationMessage command = jobQueue.poll(timeout, TimeUnit.MILLISECONDS);
				if(command != null) {
					log.info("Command received: " + command);
					if (command.getJobType().equals(JobType.COMMAND)) {
						commandLine = new CommandLineExecution();
						commandLine.setCommand(command);
					}
					threadPool.submit(commandLine);
				} else {
					log.info("Timeout waiting for jobQueue");
				}
			} catch (InterruptedException e) { 
				log.error("RunnerThread sleep was interrupted", e);
			}
		} while(continueWaiting);

	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (threadPool == null) {
			createThreadPool();
		}
	}
	private void createThreadPool() {
		threadPool = Executors.newFixedThreadPool(numberOfThreads);
	}
	
	public void stopExecutions() {
		threadPool.shutdownNow();
		continueWaiting = false;
	}

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }

    public void setJobQueue(BlockingQueue<JobInitiationMessage> jobQueue) {
        this.jobQueue = jobQueue;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
