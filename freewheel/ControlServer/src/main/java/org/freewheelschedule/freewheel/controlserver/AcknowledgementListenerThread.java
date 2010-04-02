/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.controlserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;

import java.io.IOException;
import java.net.SocketTimeoutException;

import static org.freewheelschedule.freewheel.common.message.Conversation.ACKNOWLEDGEMENT;
import static org.freewheelschedule.freewheel.common.message.Conversation.HELO;

public class AcknowledgementListenerThread implements Runnable {

    private static Log log = LogFactory.getLog(AcknowledgementListenerThread.class);
    private FreewheelSocket inboundSocket;
    private boolean continueWaiting = true;

    @Override
    public void run() {
        String conversation;

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
                    log.info("Message from client: " + conversation);
                    inboundSocket.writeSocket(ACKNOWLEDGEMENT + "\r\n");
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

    public void setInboundSocket(FreewheelSocket inboundSocket) {
        this.inboundSocket = inboundSocket;
    }

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }
}
