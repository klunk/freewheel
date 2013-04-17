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

import org.codehaus.jackson.map.ObjectMapper;
import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.model.Job;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.List;

import static org.freewheelschedule.freewheel.common.util.ApplicationContextProvider.getApplicationContext;

@Path("/jobs")
public class JobServices {

    ObjectMapper mapper = new ObjectMapper();

    @GET
    @Produces("application/json")
    public String getJobs() throws IOException {
        JobDao jobDao = (JobDao) getApplicationContext().getBean("jobDao");
        List<Job> jobs = jobDao.read();
        return mapper.writeValueAsString(jobs);
    }
}
