package org.freewheelschedule.freewheel.common.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="EXECUTION")
public class Execution {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long uid;

    @Column
    Status status;

    @Column
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
