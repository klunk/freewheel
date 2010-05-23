package org.freewheelschedule.freewheel.common.model;

import org.hamcrest.Description;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@DiscriminatorValue("REPEATING")
public class RepeatingTrigger extends Trigger<RepeatingTrigger>  {
    @Column
    Date triggerTime;
    @Column
    Long triggerInterval;

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Long getTriggerInterval() {
        return triggerInterval;
    }

    public void setTriggerInterval(Long triggerInterval) {
        this.triggerInterval = triggerInterval;
    }

    @Override
    public boolean matchesSafely(RepeatingTrigger trigger) {
        return this.triggerTime.equals(trigger.getTriggerTime());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Repeating Trigger Matcher");
    }

    @Override
    public boolean isTriggered() {
        Date now = new GregorianCalendar().getTime();
        return (triggerTime.compareTo(now) < 0);
    }
}