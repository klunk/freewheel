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

package org.freewheelschedule.freewheel.remoteworker;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;
import org.freewheelschedule.freewheel.common.network.Listener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

import static org.freewheelschedule.freewheel.common.message.Conversation.*;

public class ListenerThread implements Listener, Runnable {

	private static Log log = LogFactory.getLog(ListenerThread.class);
	
	private BlockingQueue<JobInitiationMessage> jobQueue;
	private FreewheelSocket inboundSocket;
	private boolean continueWaiting = true;
    private Gson gson = new Gson();

    public void run() {
		
		String conversation;
		
		log.info("Freewheel RemoteWorker listening on port " + inboundSocket.getPort() + " ...");
		
		do {
			log.debug("In the Listener thread");
			try {
				inboundSocket.waitSocket();
				log.info("Accepted contact from " + inboundSocket.getRemoteMachineName());
				inboundSocket.writeSocket(HELO + "\r\n");
				
				conversation = inboundSocket.readSocket();
				if (conversation.contains(HELO)) {
					inboundSocket.writeSocket(COMMAND + "\r\n");
					conversation = inboundSocket.readSocket();
					log.info("Message from client: " + conversation);
					JobInitiationMessage jobDetails = gson.fromJson(conversation, JobInitiationMessage.class);
					jobQueue.put(jobDetails);
                    inboundSocket.writeSocket(CONFIRMATION + "\r\n");
				} else {
					log.info("Invalid response from client: " + conversation);
				}
			} catch (SocketTimeoutException ste) {
				log.info("Socket timed out on accept, continue waiting: " + continueWaiting);
			} catch (IOException e) {
				log.error("Error while waiting for instructions", e);
				return;
			} catch (InterruptedException e1) {
				log.error("Error while processing instruction", e1);
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

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }

    public void setInboundSocket(FreewheelSocket inboundSocket) {
        this.inboundSocket = inboundSocket;
    }

    public void setJobQueue(BlockingQueue<JobInitiationMessage> jobQueue) {
        this.jobQueue = jobQueue;
    }
}
