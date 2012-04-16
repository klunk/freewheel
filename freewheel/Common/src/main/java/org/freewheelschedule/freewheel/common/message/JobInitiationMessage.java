

/*
 * Copyright 2012 Copyright SixRQ Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.freewheelschedule.freewheel.common.message;

import org.freewheelschedule.freewheel.common.helper.UidGenerator;

public class JobInitiationMessage {

    private Long uid;
	private JobType jobType;
	private String command;
	private String stdout;
	private Boolean appendStdout = false;
	private String stderr;
	private Boolean appendStderr = false;
	private String serverMachine;
	private Integer serverPort;

    public JobInitiationMessage() {
        uid = UidGenerator.getNewUid();
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
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

    public void setAppendStdout(boolean appendStdout) {
        this.appendStdout = appendStdout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobInitiationMessage)) return false;

        JobInitiationMessage that = (JobInitiationMessage) o;

        if (appendStderr != null ? !appendStderr.equals(that.appendStderr) : that.appendStderr != null) return false;
        if (appendStdout != null ? !appendStdout.equals(that.appendStdout) : that.appendStdout != null) return false;
        if (command != null ? !command.equals(that.command) : that.command != null) return false;
        if (jobType != that.jobType) return false;
        if (serverMachine != null ? !serverMachine.equals(that.serverMachine) : that.serverMachine != null)
            return false;
        if (serverPort != null ? !serverPort.equals(that.serverPort) : that.serverPort != null) return false;
        if (stderr != null ? !stderr.equals(that.stderr) : that.stderr != null) return false;
        if (stdout != null ? !stdout.equals(that.stdout) : that.stdout != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = jobType != null ? jobType.hashCode() : 0;
        result = 31 * result + (command != null ? command.hashCode() : 0);
        result = 31 * result + (stdout != null ? stdout.hashCode() : 0);
        result = 31 * result + (appendStdout != null ? appendStdout.hashCode() : 0);
        result = 31 * result + (stderr != null ? stderr.hashCode() : 0);
        result = 31 * result + (appendStderr != null ? appendStderr.hashCode() : 0);
        result = 31 * result + (serverMachine != null ? serverMachine.hashCode() : 0);
        result = 31 * result + (serverPort != null ? serverPort.hashCode() : 0);
        return result;
    }
}
