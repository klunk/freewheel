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

package org.freewheelschedule.freewheel.controlserver;

import org.freewheelschedule.freewheel.common.dao.TriggerDao;
import org.freewheelschedule.freewheel.common.model.Trigger;

import java.util.concurrent.BlockingQueue;

public abstract class FreewheelAbstractRunnable implements Runnable {

    protected boolean continueWaiting = true;
    protected BlockingQueue<Trigger> triggerQueue;
    protected TriggerDao triggerDao;

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }

    public void setTriggerQueue(BlockingQueue<Trigger> triggerQueue) {
        this.triggerQueue = triggerQueue;
    }

    public void setTriggerDao(TriggerDao triggerDao) {
        this.triggerDao = triggerDao;
    }


}
