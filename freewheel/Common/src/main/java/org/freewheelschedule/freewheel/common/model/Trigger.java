package org.freewheelschedule.freewheel.common.model;

import org.hamcrest.TypeSafeMatcher;

public abstract class Trigger<T> extends TypeSafeMatcher<T> {

    TriggerType triggerType;

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }
}
