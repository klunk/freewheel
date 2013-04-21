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

public class ExecutionListBuilder implements JaxbBuilder<ExecutionList, List<org.freewheelschedule.freewheel.common.model.Execution>> {

    ExecutionBuilder mapper= new ExecutionBuilder();

    @Override
    public ExecutionList build(List<org.freewheelschedule.freewheel.common.model.Execution> source, boolean mapCollections) {
        ExecutionList executionList = new ExecutionList();
        for(org.freewheelschedule.freewheel.common.model.Execution execution : source) {
            executionList.getExecutions().add(mapper.build(execution, mapCollections));
        }
        return executionList;
    }
}
