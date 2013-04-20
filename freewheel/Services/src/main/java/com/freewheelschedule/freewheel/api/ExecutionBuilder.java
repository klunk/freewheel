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

import java.util.GregorianCalendar;

public class ExecutionBuilder implements JaxbBuilder<Execution, org.freewheelschedule.freewheel.common.model.Execution> {

    @Override
    public Execution build(org.freewheelschedule.freewheel.common.model.Execution source, boolean mapCollections) {
        Execution execution = new Execution();
        execution.setUid(source.getUid());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(source.getExecutionTime());
        execution.setExecutionTime(new XMLGregorianCalendarImpl(calendar));
        execution.setStatus(source.getStatus().name());
        return execution;
    }
}
