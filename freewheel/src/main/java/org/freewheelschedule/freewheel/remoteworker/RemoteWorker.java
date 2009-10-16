/**
 * This class is used to start the RemoteWorker service. It contains
 * just a main method which starts a new thread process and a shutdown 
 * hook which will be used to stop the server process and all its threads
 *
 * @author wiehsim
 *
 */
package org.freewheelschedule.freewheel.remoteworker;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RemoteWorker {

	private final static Log log = LogFactory.getLog(RemoteWorker.class);
	
	private @Setter int numberOfWorkers;
	private @Setter int port;

	private Thread runnerThread;
	
	public void runRemoteWorker() {
		
		log.info("Freewheel RemoteWroker running ....");
		Runnable runner = new ListenerThread();

		log.info("Registering the shutdown hook");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				log.info("Shutting down the RemoteWorker ...");
			}
		});
		
		if (runner instanceof ListenerThread) {
			((ListenerThread) runner).setPort(port);
		}
		
		runnerThread = new Thread(runner);
		
		runnerThread.start();
		try {
			runnerThread.join();
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