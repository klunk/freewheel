/*
 * Copyright (c) 2012 SixRQ Ltd.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
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

        log.info("Freewheel RemoteWorker has stopped.");

	}

    public void joinListenerThread() {
        try {
            listenerThread.join();
        } catch (InterruptedException e) {
            log.error("Join interrupted", e);
        }
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
        worker.joinListenerThread();
	}

}
