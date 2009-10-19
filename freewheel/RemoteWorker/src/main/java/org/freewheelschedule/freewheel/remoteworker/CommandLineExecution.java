package org.freewheelschedule.freewheel.remoteworker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommandLineExecution implements Execution {

	private static Log log = LogFactory.getLog(CommandLineExecution.class);
	
	private @Setter String command;
	
	@Override
	public void run() {

		String message = null;
		log.info("Running command " + command);

		try {
			Process process = Runtime.getRuntime().exec(command);
			
			// getInputStream() returns the stdout of the process that ran
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((message = stdOut.readLine()) != null) {
				log.info("stdout: " + message);
			}
			while ((message = stdErr.readLine()) != null) {
				log.info("stderr: " + message);
			}
		} catch (IOException e) {
			log.error("Execution failed", e);
		}

	}

}
