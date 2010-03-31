package org.freewheelschedule.freewheel.remoteworker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

public class ListenerThread implements Listener, Runnable {

	private static Log log = LogFactory.getLog(ListenerThread.class);
	
	private BlockingQueue<JobInitiationMessage> jobQueue;
	private FreewheelSocket inboundSocket;
	private boolean continueWaiting = true;
	
	public void run() {
		
		String conversation;
		
		log.info("Freewheel RemoteWorker listening on port " + inboundSocket.getPort() + " ...");
		
		do {
			log.debug("In the Listener thread");
			try {
				inboundSocket.waitSocket();
				log.info("Accepted contact from " + inboundSocket.getRemoteMachineName());
				inboundSocket.writeSocket("HELO\r\n");
				
				conversation = inboundSocket.readSocket();
				if (conversation.contains("HELO " + inboundSocket.getRemoteMachineName())) {
					inboundSocket.writeSocket("Enter command to run\r\n");
					conversation = inboundSocket.readSocket();
					log.info("Message from client: " + conversation);
					JobInitiationMessage jobDetails = new JobInitiationMessage(conversation);
					jobQueue.put(jobDetails);
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
