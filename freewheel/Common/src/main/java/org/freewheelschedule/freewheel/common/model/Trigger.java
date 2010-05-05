package org.freewheelschedule.freewheel.common.model;

import org.hamcrest.TypeSafeMatcher;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TRIGGER")
public abstract class Trigger<T> extends TypeSafeMatcher<T> {

    @Column
    TriggerType triggerType;

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }
}
