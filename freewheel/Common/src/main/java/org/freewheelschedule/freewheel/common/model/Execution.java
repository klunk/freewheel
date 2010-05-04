package org.freewheelschedule.freewheel.common.model;

import java.util.Date;

public class Execution {
    Status status;
    Date executionTime;

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
