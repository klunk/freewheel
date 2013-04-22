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
import org.freewheelschedule.freewheel.common.util.DayOfWeek;
import org.joda.time.LocalTime;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.freewheelschedule.freewheel.common.model.TriggerType.REPEATING;
import static org.freewheelschedule.freewheel.common.model.TriggerType.TIMED;

public class TriggerBuilder implements JaxbBuilder<Trigger, org.freewheelschedule.freewheel.common.model.Trigger> {

    @Override
    public Trigger build(org.freewheelschedule.freewheel.common.model.Trigger source, boolean mapCollections) {
        Trigger trigger = getNewTriggerFromSource(source);
        trigger.setUid(source.getUid());
        trigger.setTriggerType(TriggerType.fromValue(source.getType().getValue()));
        if (mapCollections) {
            JobBuilder jobMapper = new JobBuilder();
            trigger.setJob(jobMapper.build(source.getJob(), false));
        }
        return trigger;
    }

    private Trigger getNewTriggerFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        Trigger trigger = null;
        if (source.getType() == TIMED) {
            trigger = new TimedTrigger();
            ((TimedTrigger) trigger).setTriggerTime(getTriggerTimeFromSource(source));
            List<String> daysOfWeek = getDaysOfWeekFromSource(source);
            if (daysOfWeek != null) {
                ((TimedTrigger) trigger).getDaysOfWeeks().addAll(daysOfWeek);
            }
            ((TimedTrigger) trigger).setRunDate(getRunDateFromSource(source));
        }
        if (source.getType() == REPEATING) {
            trigger = new RepeatingTrigger();
            ((RepeatingTrigger) trigger).setTriggerTime(getTriggerTimeFromSource(source));
            ((RepeatingTrigger) trigger).setTriggerInterval(getTriggerIntervalFromSource(source));
        }
        return trigger;
    }

    private XMLGregorianCalendar getRunDateFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        if (((org.freewheelschedule.freewheel.common.model.TimedTrigger) source).getRunDate() != null) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(((org.freewheelschedule.freewheel.common.model.TimedTrigger) source).getRunDate().toDate());
            return new XMLGregorianCalendarImpl(calendar);
        }
        return null;
    }

    private List<String> getDaysOfWeekFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        int daysOfWeek = ((org.freewheelschedule.freewheel.common.model.TimedTrigger) source).getDaysOfWeek();
        List<String> days = new ArrayList<String>();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.matches(daysOfWeek)) {
                days.add(day.name());
            }
        }
        return days;
    }

    private Long getTriggerIntervalFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        return ((org.freewheelschedule.freewheel.common.model.RepeatingTrigger) source).getTriggerInterval();
    }

    private String getTriggerTimeFromSource(org.freewheelschedule.freewheel.common.model.Trigger source) {
        LocalTime triggerTime = ((org.freewheelschedule.freewheel.common.model.TriggerWithTime) source).getTriggerTime();
        return triggerTime == null ? null :
                String.format("%2d:%02d:%02d.%03d",
                        triggerTime.getHourOfDay(),
                        triggerTime.getMinuteOfHour(),
                        triggerTime.getSecondOfMinute(),
                        triggerTime.getMillisOfSecond());
    }
}
