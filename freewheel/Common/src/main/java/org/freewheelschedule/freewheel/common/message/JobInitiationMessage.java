package org.freewheelschedule.freewheel.common.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JobInitiationMessage {

	private final static Log log = LogFactory.getLog(JobInitiationMessage.class);
	
	private JobType jobType;
	private String command;
	private String stdout;
	private Boolean appendStdout = false;
	private String stderr;
	private Boolean appendStderr = false;
	private String serverMachine;
	private Integer serverPort;
	
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


    public String getCommand() {
        return command;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public boolean getAppendStdout() {
        return appendStdout;
    }

    public boolean getAppendStderr() {
        return appendStderr;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public void setAppendStderr(boolean appendStderr) {
        this.appendStderr = appendStderr;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }
}
