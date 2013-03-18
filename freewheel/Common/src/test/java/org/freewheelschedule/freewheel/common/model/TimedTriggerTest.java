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

package org.freewheelschedule.freewheel.common.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TimedTriggerTest {

    TimedTrigger triggerUnderTest = new TimedTrigger();

    @Test
    public void testIsCorrectlyTriggered() throws Exception {
        triggerUnderTest.setTriggerTime(new LocalTime());
        assertThat(triggerUnderTest.isTriggered(), is(equalTo(true)));
    }

    @Test
    public void testFutureTimeIsNotTriggered() throws Exception {
        triggerUnderTest.setTriggerTime((new LocalTime()).plusMinutes(5));
        assertThat(triggerUnderTest.isTriggered(), is(equalTo(false)));
    }

    @Before
    public void setUp() {
        String dayOfWeek = (new LocalDate()).dayOfWeek().getAsText();
        int today = DayOfWeek.valueOf(dayOfWeek.toUpperCase()).getDayValue();
        triggerUnderTest.setDaysOfWeek(today);
    }
}

