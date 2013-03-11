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

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.dao.TriggerDao;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.freewheelschedule.freewheel.common.model.CommandJob;
import org.freewheelschedule.freewheel.common.model.Job;
import org.freewheelschedule.freewheel.common.model.RepeatingTrigger;
import org.freewheelschedule.freewheel.common.model.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import static org.freewheelschedule.freewheel.common.message.Conversation.*;


public class ControlServer {

    private final static Log log = LogFactory.getLog(ControlServer.class);

    private Gson gson = new Gson();
    private Runnable listener;

    private Runnable controller;

    private BlockingQueue<Trigger> triggerQueue = new PriorityBlockingQueue<Trigger>();
    private Thread listenerThread;
    private Long timeout = 500L;
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

        log.info("Strating threads");
        listenerThread.start();
        controllerThread.start();
        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            log.error("ControlServer interrupted waiting for jobs", e);
        }
    }

    public void setController(Runnable controller) {
        this.controller = controller;
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    /**
     * main method to start the ControlServer process.
     *
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-ControlServer.xml");

        ControlServer server = (ControlServer) ctx.getBean("controlServer");
        server.runControlServer();

    }
}
