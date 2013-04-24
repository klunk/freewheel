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
import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExecutionBuilderTest {
    ExecutionBuilder objectUnderTest = new ExecutionBuilder();

    @Test
    public void testBuild() {
        GregorianCalendar executionTime = new GregorianCalendar(2013, 2, 1, 3, 50, 12);
        XMLGregorianCalendar expectedTime = new XMLGregorianCalendarImpl(executionTime);
        org.freewheelschedule.freewheel.common.model.Status started = org.freewheelschedule.freewheel.common.model.Status.STARTED;
        long uid = 123L;

        org.freewheelschedule.freewheel.common.model.Execution source = new org.freewheelschedule.freewheel.common.model.Execution();
        source.setStatus(started);
        source.setUid(uid);
        source.setExecutionTime(executionTime.getTime());

        Execution actualResult = objectUnderTest.build(source, false);
        assertThat(actualResult.getExecutionTime(), is(equalTo(expectedTime)));
        assertThat(actualResult.getStatus().value(), is(equalTo(started.name())));
        assertThat(actualResult.getUid(), is(equalTo(uid)));
    }
}
