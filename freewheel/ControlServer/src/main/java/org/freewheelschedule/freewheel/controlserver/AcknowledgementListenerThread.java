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
import org.freewheelschedule.freewheel.common.dao.ExecutionDao;
import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.dao.TriggerDao;
import org.freewheelschedule.freewheel.common.message.JobResponseMessage;
import org.freewheelschedule.freewheel.common.model.*;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingQueue;

import static org.freewheelschedule.freewheel.common.message.Conversation.ACKNOWLEDGEMENT;
import static org.freewheelschedule.freewheel.common.message.Conversation.HELO;

public class AcknowledgementListenerThread extends FreewheelAbstractRunnable {

    private static Log log = LogFactory.getLog(AcknowledgementListenerThread.class);
    private FreewheelSocket inboundSocket;

    private JobDao jobDao;

    private ExecutionDao executionDao;

    @Override
    public void run() {
        String conversation;
        Gson gson = new Gson();

        log.info("Freewheel ControlServer listening on port " + inboundSocket.getPort() + " ...");

        do {
            log.debug("In the Listener thread");
            try {
                inboundSocket.waitSocket();
                log.info("Accepted contact from " + inboundSocket.getRemoteMachineName());
                inboundSocket.writeSocket(HELO + "\r\n");

                conversation = inboundSocket.readSocket();
                if (conversation.contains(HELO)) {
                    conversation = inboundSocket.readSocket();
                    JobResponseMessage responseMessage = gson.fromJson(conversation, JobResponseMessage.class);
                    log.debug("Json from client: " + conversation);
                    log.info("Message from client: " + responseMessage.getMessage());
                    inboundSocket.writeSocket(ACKNOWLEDGEMENT + "\r\n");
                    storeExecutionStatus(responseMessage);
                    if (responseMessage.getStatus() == Status.SUCCESS) {
                        setNextTrigger(responseMessage);
                    }
                } else {
                    log.info("Invalid response from client: " + conversation);
                }
            } catch (SocketTimeoutException ste) {
                log.info("Socket timed out on accept, continue waiting: " + continueWaiting);
            } catch (IOException e) {
                log.error("Error while waiting for instructions", e);
                return;
            } finally {
                try {
                    inboundSocket.close();
                } catch (IOException e) {
                    log.error("Unable to close the socket", e);
                }
            }

        } while (continueWaiting);
    }

    private void storeExecutionStatus(JobResponseMessage responseMessage) {
        Job job = jobDao.readById(responseMessage.getUid());
        Execution execution = new Execution();
        execution.setExecutionTime((new GregorianCalendar()).getTime());
        execution.setStatus(responseMessage.getStatus());
        executionDao.create(execution);
        job.getExecutions().add(execution);
        jobDao.create(job);
    }

    private void setNextTrigger(JobResponseMessage responseMessage) {
        Trigger trigger = triggerDao.readByJobId(responseMessage.getUid());
        if (trigger instanceof RepeatingTrigger) {
            LocalTime triggerTime = new LocalTime().plusMillis(((RepeatingTrigger) trigger).getTriggerInterval().intValue());
            ((RepeatingTrigger) trigger).setTriggerTime(triggerTime);
            try {
                triggerQueue.put(trigger);
            } catch (InterruptedException e) {
                log.error("Interrupted adding trigger to Queue", e);
            }
        }
    }


    public void setInboundSocket(FreewheelSocket inboundSocket) {
        this.inboundSocket = inboundSocket;
    }

    public void setJobDao(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public void setExecutionDao(ExecutionDao executionDao) {
        this.executionDao = executionDao;
    }

}
