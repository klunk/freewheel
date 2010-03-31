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
