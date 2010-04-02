/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.remoteworker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.freewheelschedule.freewheel.common.message.Conversation.COMPLETE;
import static org.freewheelschedule.freewheel.common.message.Conversation.HELO;

public class CommandLineExecution implements Execution {

	private static Log log = LogFactory.getLog(CommandLineExecution.class);
	
	private JobInitiationMessage command;
    private int remotePort;

    @Override
	public void run() {

		String message;
		PrintWriter stdoutOutput = null;
		PrintWriter stderrOutput = null;

        String hostname;
        try {
            hostname = (InetAddress.getLocalHost()).getCanonicalHostName();
        } catch (UnknownHostException e1) {
            log.error("Unable to determine hostname",e1);
            return;
        }

		log.info("Running command " + command);

		try {
			Process process = Runtime.getRuntime().exec(command.getCommand());
			
			if (command.getStdout()!= null) {
				stdoutOutput = new PrintWriter(new FileOutputStream(command.getStdout(),command.getAppendStdout()));
			}
			if (command.getStderr() != null) {
				stderrOutput = new PrintWriter(new FileOutputStream(command.getStderr(),command.getAppendStderr()));
			}
			// getInputStream() returns the stdout of the process that ran
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((message = stdOut.readLine()) != null) {
				log.info("stdout: " + message);
				if (stdoutOutput != null) {
					stdoutOutput.write(message+"\n");
				}
			}
			while ((message = stdErr.readLine()) != null) {
				log.info("stderr: " + message);
				if (stderrOutput != null) {
					stderrOutput.write(message+"\n");
				}
			}
			if (stderrOutput != null) {
				stderrOutput.close();
				stderrOutput = null;
			}
			if (stdoutOutput != null) {
				stdoutOutput.close();
				stdoutOutput = null;
			}

            Socket remoteWorker = new Socket(hostname, remotePort);

            PrintWriter speak = new PrintWriter(remoteWorker.getOutputStream(), true);
            BufferedReader result = new BufferedReader(new InputStreamReader(remoteWorker.getInputStream()));

            String response = result.readLine();
            if (response.equals(HELO)) {
                speak.print(HELO + " " + hostname + "\r\n");
                speak.flush();
                speak.print(COMPLETE + " " + command.getUid() + "\r\n");
                speak.flush();
            } else {
                log.error("Unexpected response from ControlServer");
                return;
            }

		} catch (IOException e) {
			log.error("Execution failed", e);
		} finally {
			if (stderrOutput != null) {
				stderrOutput.close();
			}
			if (stdoutOutput != null) {
				stdoutOutput.close();
			}
		}

	}

    @Override
    public void setCommand(JobInitiationMessage command) {
        this.command = command;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
}
