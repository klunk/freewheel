package org.freewheelschedule.freewheel.common.model;

import org.hamcrest.Description;

import java.util.Date;

import static org.freewheelschedule.freewheel.common.model.TriggerType.TIMED;

public class TimedTrigger extends Trigger<TimedTrigger>  {
    Date triggerTime;

    public TimedTrigger() {
        triggerType = TIMED;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Override
    public boolean matchesSafely(TimedTrigger trigger) {
        return this.triggerTime.equals(trigger.getTriggerTime());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Timed Trigger Matcher");
    }
}
