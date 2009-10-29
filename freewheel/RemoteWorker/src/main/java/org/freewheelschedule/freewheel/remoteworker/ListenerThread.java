package org.freewheelschedule.freewheel.remoteworker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;

public class ListenerThread implements Listener, Runnable {

	private static Log log = LogFactory.getLog(ListenerThread.class);
	
	private @Setter BlockingQueue<JobInitiationMessage> jobQueue;
	private @Setter FreewheelSocket inboundSocket;
	
	public void run() {
		
		String conversation = null;
		
		log.info("Freewheel RemoteWorker listening on port " + inboundSocket.getPort() + " ...");
		
		while(true) {
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

		}
	}

}
