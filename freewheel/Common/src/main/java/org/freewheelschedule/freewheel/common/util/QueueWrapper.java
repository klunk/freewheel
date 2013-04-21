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

package org.freewheelschedule.freewheel.common.util;

import org.freewheelschedule.freewheel.common.model.Trigger;
import org.freewheelschedule.freewheel.common.model.TriggerType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class QueueWrapper {
    private Map<TriggerType, BlockingQueue<Trigger>> queueMap = new HashMap<TriggerType, BlockingQueue<Trigger>>();

    protected void populateQueueMap(Map<TriggerType, BlockingQueue<Trigger>> queueMap) {
        this.queueMap = queueMap;
    }

    public void setQueueMap(Map<TriggerType, PriorityBlockingQueue<Trigger>> queueMap) {
        for (TriggerType triggerType : queueMap.keySet()) {
            this.queueMap.put(triggerType, queueMap.get(triggerType));
        }
    }

    public Map<TriggerType, BlockingQueue<Trigger>> getQueueMap() {
        return queueMap;
    }

    public void put(Trigger trigger) throws InterruptedException {
        queueMap.get(trigger.getType()).put(trigger);
    }

    public void remove(Trigger firedTrigger) {
        queueMap.get(firedTrigger.getType()).remove(firedTrigger);
    }

    public Trigger getNextTrigger() {
        for (TriggerType triggerType : queueMap.keySet()) {
            Trigger nextInLine = queueMap.get(triggerType).peek();
            if (nextInLine != null && nextInLine.isTriggered()) {
                return nextInLine;
            }
        }
        return null;
    }
}
