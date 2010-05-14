package org.freewheelschedule.freewheel.common.model;

import org.hamcrest.Description;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("TIMED")
public class TimedTrigger extends Trigger<TimedTrigger>  {
    @Column
    Date triggerTime;

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
