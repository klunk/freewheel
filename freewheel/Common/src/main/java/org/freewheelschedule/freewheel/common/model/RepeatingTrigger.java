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

import org.hamcrest.Description;
import org.joda.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@DiscriminatorValue("REPEATING")
public class RepeatingTrigger extends TriggerWithTime<RepeatingTrigger>  {
    @Column
    Long triggerInterval;

    public RepeatingTrigger() {
        super.setType(TriggerType.REPEATING);
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
    public boolean resetTrigger() {
        LocalTime triggerTime = new LocalTime().plusMillis(getTriggerInterval().intValue());
        setTriggerTime(triggerTime);
        return true;
    }

    @Override
    public int compareTo(RepeatingTrigger that) {
        return this.getTriggerTime().compareTo(that.getTriggerTime());
    }
}