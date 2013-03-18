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

import static org.freewheelschedule.freewheel.common.model.DayOfWeek.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.List;


public class DayOfWeekTest {
    @Test
    public void testIsThisDay() throws Exception {
        assertThat(MONDAY.matches(2), is(equalTo(true)));
        assertThat(MONDAY.matches(3), is(equalTo(true)));
        assertThat(SUNDAY.matches(3), is(equalTo(true)));
        assertThat(SUNDAY.matches(5), is(equalTo(true)));
        assertThat(TUESDAY.matches(5), is(equalTo(true)));
        assertThat(WEDNESDAY.matches(8), is(equalTo(true)));
        assertThat(MONDAY.matches(18), is(equalTo(true)));
        assertThat(THURSDAY.matches(18), is(equalTo(true)));
        assertThat(FRIDAY.matches(32), is(equalTo(true)));
    }

    @Test
    public void testGetDaysFromValue() throws Exception {
        List<DayOfWeek> days = DayOfWeek.getDaysFromValue(25);
        assertThat(days.size(), is(equalTo(3)));
        assertThat(days.contains(SUNDAY), is(equalTo(true)));
        assertThat(days.contains(WEDNESDAY), is(equalTo(true)));
        assertThat(days.contains(THURSDAY), is(equalTo(true)));
    }
}
