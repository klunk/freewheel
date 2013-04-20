/*
 * Copyright (c) 2013 SixRQ Ltd.
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

package com.freewheelschedule.freewheel.api;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.freewheelschedule.freewheel.common.model.RepeatingTrigger;
import org.freewheelschedule.freewheel.common.model.TimedTrigger;
import org.freewheelschedule.freewheel.common.model.TriggerWithTime;
import org.freewheelschedule.freewheel.common.util.DayOfWeek;
import org.joda.time.LocalTime;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class TriggerBuilder implements JaxbBuilder<Trigger, org.freewheelschedule.freewheel.common.model.Trigger> {

    @Override
    public Trigger build(org.freewheelschedule.freewheel.common.model.Trigger source, boolean mapCollections) {
        Trigger trigger = new Trigger();
        trigger.setUid(source.getUid());
        trigger.setTriggerType(source.getType().getValue());
        trigger.setTriggerTime(getTriggerTimeFromSource(source));
        trigger.setTriggerInterval(getTriggerIntervalFromSource(source));
        List<String> daysOfWeek = getDaysOfWeekFromSource(source);
        if (daysOfWeek != null) {
            trigger.getDaysOfWeek().addAll(daysOfWeek);
        }
        trigger.setRunDate(getRunDateFromSource(source));
        if (mapCollections) {
            JobBuilder jobMapper = new JobBuilder();
            trigger.setJob(jobMapper.build(source.getJob(), false));
        }
        return trigger;
    }

    private XMLGregorianCalendar getRunDateFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        if (source instanceof TimedTrigger && ((TimedTrigger) source).getRunDate() != null) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(((TimedTrigger) source).getRunDate().toDate());
            return new XMLGregorianCalendarImpl(calendar);
        }
        return null;
    }

    private List<String> getDaysOfWeekFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        if (source instanceof TimedTrigger) {
            int daysOfWeek = ((TimedTrigger) source).getDaysOfWeek();
            List<String> days = new ArrayList<String>();
            for (DayOfWeek day : DayOfWeek.values()) {
                if (day.matches(daysOfWeek)) {
                    days.add(day.name());
                }
            }
            return days;
        }
        return null;
    }

    private Long getTriggerIntervalFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        if (source instanceof RepeatingTrigger) {
            return ((RepeatingTrigger) source).getTriggerInterval();
        }
        return null;
    }

    private String getTriggerTimeFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        if (source instanceof TriggerWithTime) {
            LocalTime triggerTime = ((TriggerWithTime) source).getTriggerTime();
            return triggerTime == null ? null :
                    String.format("%2d:%02d:%02d.%03d",
                            triggerTime.getHourOfDay(),
                            triggerTime.getMinuteOfHour(),
                            triggerTime.getSecondOfMinute(),
                            triggerTime.getMillisOfSecond());
        }
        return null;
    }
}
