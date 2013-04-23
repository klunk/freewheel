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
        Job job = getJobFromSource(source);
        job.setUid(source.getUid());
        job.setName(source.getName());
        job.setDescription(source.getDescription());
        job.setStderr(source.getStderr());
        job.setAppendStderr(source.getAppendStderr());
        job.setStdout(source.getStdout());
        job.setAppendStdout(source.getAppendStdout());
        job.setJobType(JobType.fromValue(source.getType().getValue()));
        if (mapCollections) {
            TriggerListBuilder triggerListBuilder = new TriggerListBuilder();
            job.setTriggers(triggerListBuilder.build(source.getTriggers(), false));
            ExecutionListBuilder executionListBuilder = new ExecutionListBuilder();
            job.setExecutions(executionListBuilder.build(source.getExecutions(), false));
        }
        return job;
    }

    private Job getJobFromSource(org.freewheelschedule.freewheel.common.model.Job source) {
        Job job = null;
        if (source.getType() == org.freewheelschedule.freewheel.common.model.JobType.COMMAND) {
            job = new CommandJob();
            ((CommandJob) job).setCommand(((org.freewheelschedule.freewheel.common.model.CommandJob)source).getCommand());
        }
        return job;
    }
}
