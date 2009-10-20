package org.freewheelschedule.freewheel.common.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public @ToString class JobInitiationMessage {

	private @Setter @Getter JobType jobType;
	private @Setter @Getter String command;
	private @Setter @Getter String stdout;
	private @Setter @Getter String stderr;
	private @Setter @Getter String serverMachine;
	private @Setter @Getter int serverPort;
	
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
				}
			}
			if (entry.contains("jobType=")) {
				if (!entry.substring(entry.indexOf("=")+1).equals("null")) {
					jobType = JobType.valueOf(entry.substring(entry.indexOf("=")+1));
				}
			}
			if (entry.contains("stdout=")) {
				if (!entry.substring(entry.indexOf("=")+1).equals("null")) {
					stdout = entry.substring(entry.indexOf("=")+1);
				}
			}
			if (entry.contains("stderror=")) {
				if (!entry.substring(entry.indexOf("=")+1).equals("null")) {
					stderr = entry.substring(entry.indexOf("=")+1);
				}
			}
			if (entry.contains("serverMachine=")) {
				if (!entry.substring(entry.indexOf("=")+1).equals("null")) {
					serverMachine = entry.substring(entry.indexOf("=")+1);
				}
			}
			if (entry.contains("serverPort=")) {
				if (!entry.substring(entry.indexOf("=")+1).equals("null")) {
					serverPort = Integer.valueOf(entry.substring(entry.indexOf("=")+1));
				}
			}
		}
	}
	
	
}
