package org.freewheelschedule.freewheel.common.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public @ToString class JobInitiationMessage {

	private final static Log log = LogFactory.getLog(JobInitiationMessage.class);
	
	private @Setter @Getter JobType jobType;
	private @Setter @Getter String command;
	private @Setter @Getter String stdout;
	private @Setter @Getter Boolean appendStdout = false;
	private @Setter @Getter String stderr;
	private @Setter @Getter Boolean appendStderr = false;
	private @Setter @Getter String serverMachine;
	private @Setter @Getter Integer serverPort;
	
	public JobInitiationMessage() {
		super();
	}

	public JobInitiationMessage(String serialized) {
		super();
		
		String[] splitCommand = serialized.split(",");
		for(String entry: splitCommand) {
			if (entry.contains("command=")) {
				if (!entry.substring(entry.indexOf("=")+1).equals("null")) {
					command = entry.substring(entry.indexOf("=")+1);
				} else {
					log.error("You must supply a command value");
					throw new JobInitiationException();
				}
			}
			if (entry.contains("jobType=")) {
				if (!entry.substring(entry.indexOf("=")+1).startsWith("null")) {
					jobType = JobType.valueOf(entry.substring(entry.indexOf("=")+1));
				}
			}
			if (entry.contains("stdout=")) {
				if (!entry.substring(entry.indexOf("=")+1).startsWith("null")) {
					stdout = entry.substring(entry.indexOf("=")+1);
				}
			}
			if (entry.contains("appendStdout=")) {
				appendStdout = Boolean.valueOf(entry.substring(entry.indexOf("=")+1));
			}
			if (entry.contains("stderr=")) {
				if (!entry.substring(entry.indexOf("=")+1).startsWith("null")) {
					stderr = entry.substring(entry.indexOf("=")+1);
				}
			}
			if (entry.contains("appendStderr=")) {
				appendStderr = Boolean.valueOf(entry.substring(entry.indexOf("=")+1));
			}
			if (entry.contains("serverMachine=")) {
				if (!entry.substring(entry.indexOf("=")+1).startsWith("null")) {
					serverMachine = entry.substring(entry.indexOf("=")+1);
				}
			}
			if (entry.contains("serverPort=")) {
				if (!entry.substring(entry.indexOf("=")+1).startsWith("null")) {
					if (entry.endsWith(")")) {
						serverPort = Integer.valueOf(entry.substring(entry.indexOf("=")+1,entry.length()-1));
					} else {
						serverPort = Integer.valueOf(entry.substring(entry.indexOf("=")+1));
					}
				}
			}
		}
	}
	
	
}
