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

import org.joda.time.LocalTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TriggerBuilderTest {
    TriggerBuilder objectUnderTest = new TriggerBuilder();

    @Test
    public void testTimedTriggerBuild() throws Exception {
        org.freewheelschedule.freewheel.common.model.TimedTrigger source = new org.freewheelschedule.freewheel.common.model.TimedTrigger();

        int daysOfWeek = 126;
        Long uid = 12345L;
        LocalTime triggerTime = new LocalTime(12, 35, 44);
        List<String> dayOfWeekList = new ArrayList<String>();

        dayOfWeekList.add("MONDAY");
        dayOfWeekList.add("TUESDAY");
        dayOfWeekList.add("WEDNESDAY");
        dayOfWeekList.add("THURSDAY");
        dayOfWeekList.add("FRIDAY");
        dayOfWeekList.add("SATURDAY");

        source.setDaysOfWeek(daysOfWeek);
        source.setUid(uid);
        source.setTriggerTime(triggerTime);
        Trigger actualResult = objectUnderTest.build(source, false);
        assertThat(actualResult.getUid(), is(equalTo(uid)));
        assertThat(actualResult.getTriggerType(), is(equalTo(TriggerType.TIMED)));
        assertThat(((TimedTrigger)actualResult).getTriggerTime(), is(equalTo("12:35:44.000")));
        assertThat(((TimedTrigger) actualResult).getDaysOfWeeks(), is(equalTo(dayOfWeekList)));
    }

    @Test
    public void testRepeatingTriggerBuild() throws Exception {
        org.freewheelschedule.freewheel.common.model.RepeatingTrigger source = new org.freewheelschedule.freewheel.common.model.RepeatingTrigger();

        Long uid = 12345L;
        Long interval = 500L;
        LocalTime triggerTime = new LocalTime(12, 35, 44);

        source.setUid(uid);
        source.setTriggerTime(triggerTime);
        source.setTriggerInterval(interval);
        Trigger actualResult = objectUnderTest.build(source, false);
        assertThat(actualResult.getUid(), is(equalTo(uid)));
        assertThat(actualResult.getTriggerType(), is(equalTo(TriggerType.REPEATING)));
        assertThat(((RepeatingTrigger)actualResult).getTriggerTime(), is(equalTo("12:35:44.000")));
        assertThat(((RepeatingTrigger)actualResult).getTriggerInterval(), is(equalTo(interval)));
    }
}
