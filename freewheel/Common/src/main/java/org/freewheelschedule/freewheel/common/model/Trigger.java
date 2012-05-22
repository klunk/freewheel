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

import org.hamcrest.TypeSafeMatcher;

import javax.persistence.*;

@Entity
@Table(name = "TRIGGER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TriggerType", discriminatorType = DiscriminatorType.STRING)
public abstract class Trigger<T> extends TypeSafeMatcher<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long uid;
    @OneToOne(fetch=FetchType.EAGER)
    Job job;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Transient
    public abstract boolean isTriggered();
}
