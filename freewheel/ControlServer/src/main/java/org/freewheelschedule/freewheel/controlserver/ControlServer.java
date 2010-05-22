/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.controlserver;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.freewheelschedule.freewheel.common.model.CommandJob;
import org.freewheelschedule.freewheel.common.model.Job;
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
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.freewheelschedule.freewheel.common.message.Conversation.*;


public class ControlServer {

    private final static Log log = LogFactory.getLog(ControlServer.class);

    private JobDao jobDao;
    private int remotePort;
    private Gson gson = new Gson();
    private Runnable listener;

    private BlockingQueue<Job> jobQueue = new LinkedBlockingQueue<Job>();
    private Thread listenerThread;
    private boolean continueWaiting;
    private Long timeout = 500L;

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void setJobDao(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public void runControlServer() {

        String hostname;
        try {
            hostname = (InetAddress.getLocalHost()).getCanonicalHostName();
        } catch (UnknownHostException e1) {
            log.error("Unable to determine hostname",e1);
            return;
        }

        log.info("Running the ControlServer ....");

        log.info("Registering the shutdown hook");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Shutting down the ControlServer ...");
                ((AcknowledgementListenerThread) listener).setContinueWaiting(false);
                continueWaiting = false;
            }
        });

        log.info("Initializing jobs");
        initializeJobs();

        listenerThread = new Thread(listener);
        listenerThread.start();

        do {
            try {
                Job jobToRun = jobQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (jobToRun != null) {
                    log.debug("Connecting to the RemoteWorker to run a command");
                    Socket remoteWorker = new Socket(jobToRun.getExecutingServer().getName(), jobToRun.getExecutingServer().getPort().intValue());

                    PrintWriter command = new PrintWriter(remoteWorker.getOutputStream(), true);
                    BufferedReader result = new BufferedReader(new InputStreamReader(remoteWorker.getInputStream()));

                    String response = result.readLine();
                    if (response.equals(HELO)) {
                        command.print(HELO + " " + hostname + "\r\n");
                        command.flush();
                    } else {
                        log.error("Unexpected response from RemoteClient");
                        return;
                    }
                    response = result.readLine();

                    if (response.equals(COMMAND) && jobToRun instanceof CommandJob) {
                        JobInitiationMessage initiation = new JobInitiationMessage();
                        initiation.setJobType(JobType.COMMAND);
                        initiation.setCommand(((CommandJob)jobToRun).getCommand());
                        initiation.setStderr(jobToRun.getStderr());
                        initiation.setAppendStderr(jobToRun.getAppendStderr());
                        initiation.setStdout(jobToRun.getStdout());
                        initiation.setAppendStdout(jobToRun.getAppendStderr());
                        log.debug(gson.toJson(initiation));
                        command.print(gson.toJson(initiation) + "\r\n");
                        command.flush();
                    }
                    response = result.readLine();

                    if (!response.equals(CONFIRMATION)) {
                        log.error("Job not queued properly");
                        return;
                    }
                }
            } catch (UnknownHostException e) {
                log.error("Unable to open socket to RemoteWorker", e);
                return;
            } catch (SocketException e) {
                log.error("RemoteWorker is currently unavailable.", e);
            } catch (IOException e) {
                log.error("Unable to communicate with RemoteWorker", e);
                return;
            } catch (InterruptedException e) {
                log.error("ControlServer interrupted waiting for jobs", e);
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error("Sleep interrupted, continuing.", e);
            }
        } while (continueWaiting);
    }

    @Transactional
    private void initializeJobs() {
        List<Job> jobs = jobDao.read();
        for (Job job : jobs) {
            try {
                jobQueue.put(job);
            } catch (InterruptedException e) {
                log.error("Failed to read Job details from the database", e);
            }
        }
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
