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

public class JobBuilder implements JaxbBuilder<Job, org.freewheelschedule.freewheel.common.model.Job> {


    @Override
    public Job build(org.freewheelschedule.freewheel.common.model.Job source, boolean mapCollections) {
        Job job = new Job();
        job.setUid(source.getUid());
        job.setName(source.getName());
        job.setDescription(source.getDescription());
        job.setStderr(source.getStderr());
        job.setAppendStderr(source.getAppendStderr());
        job.setStdout(source.getStdout());
        job.setAppendStdout(source.getAppendStdout());
        if (mapCollections) {
            TriggerListBuilder triggerListMapper = new TriggerListBuilder();
            job.setTriggers(triggerListMapper.build(source.getTriggers(), false));
            ExecutionListBuilder executionListMapper = new ExecutionListBuilder();
            job.setExecutions(executionListMapper.build(source.getExecutions(), false));
        }
        return job;
    }
}
