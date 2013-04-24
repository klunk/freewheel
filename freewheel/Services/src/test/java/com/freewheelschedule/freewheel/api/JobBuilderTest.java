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

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JobBuilderTest {
    JobBuilder objectUnderTest = new JobBuilder();

    @Test
    public void testCommandJobBuild() throws Exception {
        Long uid = 12345L;
        String description = "Some description";
        String jobName = "Job name";
        String stdout = "some stdout value";
        String stderr = "Some stderr value";
        String command ="Command to run";

        org.freewheelschedule.freewheel.common.model.CommandJob source = new org.freewheelschedule.freewheel.common.model.CommandJob();
        source.setUid(uid);
        source.setDescription(description);
        source.setAppendStderr(true);
        source.setAppendStdout(true);
        source.setName(jobName);
        source.setStdout(stdout);
        source.setStderr(stderr);
        source.setCommand(command);

        Job actualResult = objectUnderTest.build(source, false);
        assertThat(actualResult.getUid(), is(equalTo(uid)));
        assertThat(actualResult.getDescription(), is(equalTo(description)));
        assertTrue(actualResult.isAppendStdout());
        assertTrue(actualResult.isAppendStderr());
        assertThat(actualResult.getName(), is(equalTo(jobName)));
        assertThat(actualResult.getStdout(), is(equalTo(stdout)));
        assertThat(actualResult.getStderr(), is(equalTo(stderr)));
        assertThat(((CommandJob)actualResult).getCommand(), is(equalTo(command)));
    }
}
