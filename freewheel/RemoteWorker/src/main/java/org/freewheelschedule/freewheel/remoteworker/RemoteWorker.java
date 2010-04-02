/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RemoteWorker {

	private final static Log log = LogFactory.getLog(RemoteWorker.class);

    private Runnable listener;
    private Runnable runner;

    private Thread listenerThread;
    private Thread runnerThread;

    public void runRemoteWorker() {

		log.info("Freewheel RemoteWroker running ....");

		log.info("Registering the shutdown hook");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				log.info("Shutting down the RemoteWorker ...");
				((RunnerThread) runner).stopExecutions();
				((ListenerThread)listener).setContinueWaiting(false);
			}
		});

		listenerThread = new Thread(listener);
		runnerThread = new Thread(runner);

		listenerThread.start();
		runnerThread.start();
		try {
			listenerThread.join();
		} catch (InterruptedException e) {
			log.error("Join interrupted", e);
		}

		log.info("Freewheel RemoteWorker has stopped.");

	}

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public void setRunner(Runnable runner) {
        this.runner = runner;
    }

    /**
	 * main method to start the RemoteWorker processes.
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-RemoteWorker.xml");
		
		RemoteWorker worker = (RemoteWorker)ctx.getBean("remoteWorker");
		worker.runRemoteWorker();
	}

}
