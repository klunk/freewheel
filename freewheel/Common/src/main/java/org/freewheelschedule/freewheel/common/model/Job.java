package org.freewheelschedule.freewheel.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="JOB")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "JobType", discriminatorType = DiscriminatorType.STRING)
public abstract class Job implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long uid;
    @Column
    String name;
    @Column
    String description;
    @OneToMany
    List<Trigger> triggers;
    @OneToMany
    List<Execution> executions;
    @Column(nullable=true)
    String stdout;
    @Column
    Boolean appendStdout;
    @Column(nullable=true)
    String stderr;
    @Column
    Boolean appendStderr;
    @ManyToOne
    Machine executingServer;

    public Machine getExecutingServer() {
        return executingServer;
    }

    public void setExecutingServer(Machine executingServer) {
        this.executingServer = executingServer;
    }

    public Boolean getAppendStdout() {
        return appendStdout;
    }

    public void setAppendStdout(Boolean appendStdout) {
        this.appendStdout = appendStdout;
    }

    public Boolean getAppendStderr() {
        return appendStderr;
    }

    public void setAppendStderr(Boolean appendStderr) {
        this.appendStderr = appendStderr;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> executions) {
        this.executions = executions;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public String toString() {
        return "uid: " + uid.toString() + " name: " + name;
    }
}
