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

package org.freewheelschedule.freewheel.controlserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.util.QueueWrapper;
import org.springframework.beans.factory.annotation.Autowired;


public class ControlServer {

    private final static Log log = LogFactory.getLog(ControlServer.class);

    @Autowired
    private QueueWrapper triggerQueue;

    private Runnable listener;
    private Runnable controller;

    private Long timeout = 500L;

    private Thread listenerThread;
    private Thread controllerThread;

    public void runControlServer() {

        log.info("Running the ControlServer ....");

        log.info("Registering the shutdown hook");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Shutting down the ControlServer ...");
                ((FreewheelAbstractRunnable)listener).setContinueWaiting(false);
                ((FreewheelAbstractRunnable)controller).setContinueWaiting(false);
            }
        });

        ((AcknowledgementListenerThread) listener).setTriggerQueue(triggerQueue);
        listenerThread = new Thread(listener);

        ((ControlThread)controller).setTriggerQueue(triggerQueue);
        controllerThread = new Thread(controller);

        log.info("Initializing jobs");
        ((ControlThread)controller).initializeJobs();

        log.info("Starting threads");
        listenerThread.start();
        controllerThread.start();
    }

    public void joinControlServer() {
        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            log.error("ControlServer interrupted waiting for jobs", e);
        }
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public void setController(Runnable controller) {
        this.controller = controller;
    }
}
