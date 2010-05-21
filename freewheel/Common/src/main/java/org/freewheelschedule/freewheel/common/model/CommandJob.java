package org.freewheelschedule.freewheel.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CommandJob extends Job {

    @Column
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
