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

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.freewheelschedule.freewheel.common.model.CommandJob;
import org.freewheelschedule.freewheel.common.model.Job;
import org.freewheelschedule.freewheel.common.model.RepeatingTrigger;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.freewheelschedule.freewheel.common.message.Conversation.*;

public class ControlThread extends FreewheelAbstractRunnable {
    private final static Log log = LogFactory.getLog(ControlThread.class);

    private Gson gson = new Gson();

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
                Trigger firedTrigger = triggerQueue.peek();
                Job jobToRun = null;
                if (firedTrigger != null && firedTrigger.isTriggered()) {
                    jobToRun = firedTrigger.getJob();
                    triggerQueue.remove(firedTrigger);
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
                            initiation.setUid(jobToRun.getUid());
                            initiation.setJobType(JobType.COMMAND);
                            initiation.setCommand(((CommandJob) jobToRun).getCommand());
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
                } else {
                    Thread.sleep(1000);
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
                log.error("Control thread interrupted waiting for jobs", e);
            }

//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                log.error("Sleep interrupted, continuing.", e);
//            }
        } while (continueWaiting);
    }

    @Transactional
    protected void initializeJobs() {
        List<Trigger> triggers = triggerDao.read();
        for (Trigger trigger : triggers) {
            try {
                if (trigger instanceof RepeatingTrigger) {
                    Date triggerTime = new Date(new GregorianCalendar().getTimeInMillis() + ((RepeatingTrigger) trigger).getTriggerInterval());
                    ((RepeatingTrigger) trigger).setTriggerTime(triggerTime);
                }
                triggerQueue.put(trigger);
            } catch (InterruptedException e) {
                log.error("Failed to read Trigger details from the database", e);
            }
        }
    }

}
