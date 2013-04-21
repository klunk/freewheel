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

import org.freewheelschedule.freewheel.common.model.Trigger;
import org.freewheelschedule.freewheel.common.model.TriggerType;
import org.freewheelschedule.freewheel.common.util.QueueWrapper;

import java.util.ArrayList;
import java.util.List;

public class QueueListBuilder implements JaxbBuilder<QueueList, QueueWrapper> {
    private TriggerListBuilder triggerListBuilder = new TriggerListBuilder();

    @Override
    public QueueList build(QueueWrapper source, boolean mapCollections) {
        QueueList queueList = new QueueList();
        for (TriggerType triggerType : source.getQueueMap().keySet()) {
            List<Trigger> triggerList = new ArrayList<Trigger>();
            triggerList.addAll(source.getQueueMap().get(triggerType));
            queueList.getTriggerLists().add(triggerListBuilder.build(triggerList, true));
        }
        return queueList;
    }
}
