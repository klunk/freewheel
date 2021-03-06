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

import java.util.List;

public class TriggerListBuilder implements JaxbBuilder<TriggerList, List<org.freewheelschedule.freewheel.common.model.Trigger>> {

    TriggerBuilder triggerBuilder = new TriggerBuilder();

    @Override
    public TriggerList build(List<org.freewheelschedule.freewheel.common.model.Trigger> source, boolean mapCollections) {
        TriggerList triggerList = new TriggerList();
        for(org.freewheelschedule.freewheel.common.model.Trigger trigger: source) {
            triggerList.getTriggers().add(triggerBuilder.build(trigger, mapCollections));
        }
        return triggerList;
    }
}
