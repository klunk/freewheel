package org.freewheelschedule.freewheel.controlserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ControlServer {

	private final static Log log = LogFactory.getLog(ControlServer.class);

	private @Setter int remotePort;
	
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
			}
		});
		
		while (true) {
			try {
				log.debug("Connecting to the RemoteWorker to run a command");
				Socket remoteWorker = new Socket(hostname, remotePort);
				
				PrintWriter command = new PrintWriter(remoteWorker.getOutputStream(), true);
				BufferedReader result = new BufferedReader(new InputStreamReader(remoteWorker.getInputStream()));
				
				String response = result.readLine();
				if (response.equals("HELO")) {
					command.print("HELO " + hostname + "\r\n");
					command.flush();
				} else {
					log.error("Unexpected response from RemoteClient");
					return;
				}
				response = result.readLine();
				
				if (response.equals("Enter command to run")) {
					JobInitiationMessage initiation = new JobInitiationMessage();
					initiation.setJobType(JobType.COMMAND);
					initiation.setCommand("java -version");
					initiation.setStderr("stderr.log");
					initiation.setAppendStderr(true);
					initiation.setStdout("stdout.log");
					log.debug(initiation);
					command.print(initiation.toString() + "\r\n");
					command.flush();
				}
				
			} catch (UnknownHostException e) {
				log.error("Unable to open socket to RemoteWorker", e);
				return;
			} catch (SocketException e) {
				log.error("RemoteWorker is currently unavailable.", e);
			} catch (IOException e) {
				log.error("Unable to communicate with RemoteWorker", e);
				return;
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				log.error("Sleep interrupted, continuing.", e);
			}
		}
	}
	
	/**
	 * main method to start the ControlServer process.
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-ControlServer.xml");
		
		ControlServer server = (ControlServer)ctx.getBean("controlServer");
		server.runControlServer();

	}

}
