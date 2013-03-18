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

import javax.persistence.Column;
import javax.persistence.Entity;
import org.joda.time.LocalTime;

@Entity
public abstract class TriggerWithTime<T> extends Trigger<T> {
    @Column
    LocalTime triggerTime;

    public LocalTime getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(LocalTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    @Override
    public boolean isTriggered() {
        return (triggerTime.compareTo(new LocalTime()) <= 0);
    }

    @Override
    public int compareTo(Trigger that) {
        if (that instanceof TriggerWithTime) {
            return this.triggerTime.compareTo(((TriggerWithTime)that).getTriggerTime());
        }
        return -1;
    }
}
