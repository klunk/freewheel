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

package com.freewheelschedule.freewheel.services.resources;

import com.freewheelschedule.freewheel.api.JobList;
import com.freewheelschedule.freewheel.api.JobListMapper;
import org.freewheelschedule.freewheel.common.dao.JobDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.freewheelschedule.freewheel.common.util.ApplicationContextProvider.getApplicationContext;

@Path("/jobs")
public class JobServices {

    JobListMapper jobListMapper = new JobListMapper();

    @GET
    @Produces(APPLICATION_JSON)
    public JobList getJobList() throws IOException {
        JobDao jobDao = (JobDao) getApplicationContext().getBean("jobDao");
        return jobListMapper.map(jobDao.read(), true);
    }
}
