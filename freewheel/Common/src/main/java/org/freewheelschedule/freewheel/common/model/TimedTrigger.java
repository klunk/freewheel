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

import org.freewheelschedule.freewheel.common.util.DayOfWeek;
import org.hamcrest.Description;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("TIMED")
public class TimedTrigger extends TriggerWithTime<TimedTrigger> implements Comparable<TimedTrigger> {
    @Column
    int daysOfWeek;
    @Transient
    private LocalDate runDate;

    public void setDaysOfWeek(int daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    public TimedTrigger() {
        super.setType(TriggerType.TIMED);
    }

    @Override
    public boolean matchesSafely(TimedTrigger trigger) {

        return this.triggerTime.equals(trigger.getTriggerTime()) && this.runDate.equals(new LocalDate());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Timed Trigger Matcher");
    }

    @Override
    public boolean isTriggered() {
        return runDate.equals(new LocalDate()) && super.isTriggered();
    }

    @Override
    public boolean resetTrigger() {
        if (getTriggerTime().isBefore(new LocalTime())) {
            setNextRunDay();
        } else {
            runDate = new LocalDate();
        }
        return true;
    }

    private void setNextRunDay() {
        LocalDate nextCandidate = new LocalDate();
        LocalDate nextRunDay = null;
        while(nextRunDay == null) {
            nextCandidate = nextCandidate.plusDays(1);
            if (DayOfWeek.valueOf(nextCandidate.dayOfWeek().getAsText().toUpperCase()).matches(daysOfWeek)) {
                nextRunDay = nextCandidate;
            }
        }
        runDate = nextRunDay;
    }

    public LocalDate getRunDate() {
        return runDate;
    }

    @Override
    public int compareTo(TimedTrigger that) {
        return this.getRunDate().compareTo(that.getRunDate());
    }
}
