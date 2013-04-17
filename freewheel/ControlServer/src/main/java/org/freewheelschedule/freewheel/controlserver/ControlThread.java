/*
 * Copyright (c) 2013 SixRQ Ltd.
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
import org.codehaus.jackson.map.ObjectMapper;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.freewheelschedule.freewheel.common.model.CommandJob;
import org.freewheelschedule.freewheel.common.model.Job;
import org.freewheelschedule.freewheel.common.model.Trigger;
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

import static org.freewheelschedule.freewheel.common.message.Conversation.*;

public class ControlThread extends FreewheelAbstractRunnable {
    private final static Log log = LogFactory.getLog(ControlThread.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run() {
        String hostname;
        try {
            hostname = (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException e1) {
            log.error("Unable to determine hostname", e1);
            return;
        }

        do {
            try {
                try {
                    Trigger firedTrigger = triggerQueue.getNextTrigger();        // Get next fired trigger from queue and run the job.
                    Job jobToRun = null;
                    if (firedTrigger != null) {
                        jobToRun = firedTrigger.getJob();
                        if (jobToRun != null) {
                            runJob(hostname, jobToRun);
                        }
                        triggerQueue.remove(firedTrigger);
                    }
                } catch (UnknownHostException e) {
                    log.error("Unable to open socket to RemoteWorker", e);
                } catch (SocketException e) {
                    log.error("RemoteWorker is currently unavailable.", e);
                } catch (IOException e) {
                    log.error("Unable to communicate with RemoteWorker", e);
                } finally {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                log.error("Control thread interrupted waiting for jobs", e);
            }
        } while (continueWaiting);
    }

    private void runJob(String hostname, Job jobToRun) throws IOException {
        log.debug("Connecting to the RemoteWorker to run a command");
        Socket remoteWorker = new Socket(jobToRun.getExecutingServer().getName(), jobToRun.getExecutingServer().getPort().intValue());

        PrintWriter command = new PrintWriter(remoteWorker.getOutputStream(), true);
        BufferedReader result = new BufferedReader(new InputStreamReader(remoteWorker.getInputStream()));

        if (workerAwaitingCommand(result, command, hostname) && jobToRun instanceof CommandJob) {
            if (!sendCommandToExecute(jobToRun, result, command)) {
                log.error("Job not queued properly");
            } else {
                log.error("Unexpected response from RemoteClient");
            }
        }
    }

    private boolean sendCommandToExecute(Job jobToRun, BufferedReader result, PrintWriter command) throws IOException {
        JobInitiationMessage initiation = new JobInitiationMessage();
        initiation.setUid(jobToRun.getUid());
        initiation.setJobType(JobType.COMMAND);
        initiation.setCommand(((CommandJob) jobToRun).getCommand());
        initiation.setStderr(jobToRun.getStderr());
        initiation.setAppendStderr(jobToRun.getAppendStderr());
        initiation.setStdout(jobToRun.getStdout());
        initiation.setAppendStdout(jobToRun.getAppendStderr());
        log.debug(mapper.writeValueAsString(initiation));
        command.print(mapper.writeValueAsString(initiation) + "\r\n");
        command.flush();
        return result.readLine().equals(CONFIRMATION);
    }


    private boolean workerAwaitingCommand(BufferedReader result, PrintWriter command, String hostname) throws IOException {
        String response = result.readLine();
        if (response.equals(HELO)) {
            command.print(HELO + " " + hostname + "\r\n");
            command.flush();
            response = result.readLine();
        }
        return response.equals(COMMAND);
    }

    @Transactional
    protected void initializeJobs() {
        List<Trigger> triggers = triggerDao.read();
        for (Trigger trigger : triggers) {
            if (trigger.resetTrigger()) {
                try {
                    triggerQueue.put(trigger);
                } catch (InterruptedException e) {
                    log.error("Failed to read Trigger details from the database", e);
                }
            }
        }
    }
}
