/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.controlserver;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.dao.TriggerDao;
import org.freewheelschedule.freewheel.common.message.JobResponseMessage;
import org.freewheelschedule.freewheel.common.model.RepeatingTrigger;
import org.freewheelschedule.freewheel.common.model.Status;
import org.freewheelschedule.freewheel.common.model.Trigger;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.BlockingQueue;

import static org.freewheelschedule.freewheel.common.message.Conversation.ACKNOWLEDGEMENT;
import static org.freewheelschedule.freewheel.common.message.Conversation.HELO;

public class AcknowledgementListenerThread implements Runnable {

    private static Log log = LogFactory.getLog(AcknowledgementListenerThread.class);
    private FreewheelSocket inboundSocket;
    private boolean continueWaiting = true;
    private BlockingQueue<Trigger> triggerQueue;

    private TriggerDao triggerDao;

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
                if (conversation.contains(HELO + " " + inboundSocket.getRemoteMachineName())) {
                    conversation = inboundSocket.readSocket();
                    JobResponseMessage responseMessage = gson.fromJson(conversation, JobResponseMessage.class);
                    log.debug("Json from client: " + conversation);
                    log.info("Message from client: " + responseMessage.getMessage());
                    inboundSocket.writeSocket(ACKNOWLEDGEMENT + "\r\n");
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

    private void setNextTrigger(JobResponseMessage responseMessage) {
        Trigger trigger = triggerDao.readByJobId(responseMessage.getUid());
        if (trigger instanceof RepeatingTrigger) {
            Date triggerTime = new Date(new GregorianCalendar().getTimeInMillis() + ((RepeatingTrigger) trigger).getTriggerInterval());
            ((RepeatingTrigger) trigger).setTriggerTime(triggerTime);
        }
        try {
            triggerQueue.put(trigger);
        } catch (InterruptedException e) {
            log.error("Interrupted adding trigger to Queue", e);
        }
    }

    public void setInboundSocket(FreewheelSocket inboundSocket) {
        this.inboundSocket = inboundSocket;
    }

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }

    public void setTriggerQueue(BlockingQueue<Trigger> triggerQueue) {
        this.triggerQueue = triggerQueue;
    }

    public void setTriggerDao(TriggerDao triggerDao) {
        this.triggerDao = triggerDao;
    }

}
