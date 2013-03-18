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

import java.util.ArrayList;
import java.util.List;

public enum DayOfWeek {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(4),
    WEDNESDAY(8),
    THURSDAY(16),
    FRIDAY(32),
    SATURDAY(64);

    private final int dayValue;

    private DayOfWeek(int dayValue) {
        this.dayValue = dayValue;
    }

    public boolean matches(int value) {
        return (dayValue & value) == dayValue;
    }

    public static List<DayOfWeek> getDaysFromValue(int value) {
        List<DayOfWeek> days = new ArrayList<DayOfWeek>();
        for (DayOfWeek day : DayOfWeek.values()) {
            if ((value & day.dayValue) == day.dayValue) days.add(day);
        }
        return days;
    }
}
