/*
 * Copyright (c) 2012 SixRQ Ltd.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.freewheelschedule.freewheel.common.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name="JOB")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "JobType", discriminatorType = DiscriminatorType.STRING)
public abstract class Job implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long uid;
    @Column(unique=true)
    String name;
    @Column
    String description;
    @OneToMany(fetch= EAGER)
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
    @ManyToOne(fetch= EAGER)
    Machine executingServer;
    @Transient
    JobType type;

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

    public JobType getType() {
        return type;
    }

    protected void setType(JobType type) {
        this.type = type;
    }

    public String toString() {
        return "uid: " + uid.toString() + " name: " + name;
    }
}
