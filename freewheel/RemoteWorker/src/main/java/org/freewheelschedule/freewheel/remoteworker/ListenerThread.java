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

public class ListenerThread implements Listener, Runnable {

	private static Log log = LogFactory.getLog(ListenerThread.class);
	
	private @Setter int port;
	private @Setter BlockingQueue<JobInitiationMessage> jobQueue;
	
	public void run() {
		
		ServerSocket socket = null;
		Socket client = null;
		BufferedReader request = null;
		PrintWriter response = null;
		String conversation = null;
		
		log.info("Freewheel RemoteWorker listening on port " + port + " ...");
		
		try {
			socket = new ServerSocket(port);
		
			while(true) {
				log.debug("In the Listener thread");
				try {
					client = socket.accept();
					log.info("Accepted contact from " + client.getInetAddress());
					request = new BufferedReader(new InputStreamReader(client.getInputStream()));
					response = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
					response.print("HELO\r\n");
					response.flush();
					
					conversation = request.readLine();
					if (conversation.contains("HELO " + client.getInetAddress())) {
						response.print("Enter command to run\r\n");
						response.flush();
						conversation = request.readLine();
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
						if (request != null) {
							request.close();
						}
						if (response != null) {
							response.close();
						}
						if (client != null) {
							client.close();
						}
					} catch (IOException e) {
						log.error("Unable to close the socket", e);
					}
				}
			}

		} catch (IOException e1) {
			log.error("Error creating a ServerSocket", e1);
			return;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				log.info("Stopping the listener");
			} catch (IOException e) {
				log.error("Unable to close the socket", e);
			}
		}
	}

}
