/**
 * This class is used to start the RemoteWorker service. It contains
 * just a main method which starts a new thread process and a shutdown 
 * hook which will be used to stop the server process and all its threads
 *
 * @author wiehsim
 *
 */
package org.freewheelschedule.freewheel.remoteworker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RemoteWorker {

	private final static Log log = LogFactory.getLog(RemoteWorker.class);
	
	private @Setter int numberOfWorkers;
	private @Setter FreewheelSocket inboundSocket;
	
	private Thread listenerThread;
	private Thread runnerThread;
	private BlockingQueue<JobInitiationMessage> jobQueue;
	
	public void runRemoteWorker() {
		
		final Runnable listener = new ListenerThread();
		final Runnable runner = new RunnerThread();
		jobQueue = new LinkedBlockingQueue<JobInitiationMessage>();
		
		log.info("Freewheel RemoteWroker running ....");

		log.info("Registering the shutdown hook");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				log.info("Shutting down the RemoteWorker ...");
				((RunnerThread) runner).stopExecutions();
			}
		});
		
		if (listener instanceof ListenerThread) {
			((ListenerThread) listener).setInboundSocket(inboundSocket);
			((ListenerThread) listener).setJobQueue(jobQueue);
			
		}
		if (runner instanceof RunnerThread) {
			((RunnerThread) runner).setNumberOfThreads(numberOfWorkers);
			((RunnerThread) runner).setJobQueue(jobQueue);
		}
		
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
