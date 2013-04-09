/*
 * Copyright (c) 2012 SixRQ Ltd.
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

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.dao.MachineDao;
import org.freewheelschedule.freewheel.common.dao.TriggerDao;
import org.freewheelschedule.freewheel.common.model.*;
import org.joda.time.LocalTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabasePopulator {

    private final static Log log = LogFactory.getLog(DatabasePopulator.class);

    private static Gson gson = new Gson();

    @Transactional
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-CommonUtil.xml");
        JobDao jobDao = (JobDao) ctx.getBean("jobDao");
        MachineDao machineDao = (MachineDao) ctx.getBean("machineDao");
        TriggerDao triggerDao = (TriggerDao) ctx.getBean("triggerDao");

        Machine machine = new Machine();
        machine.setName("localhost");
        machine.setPort(12145L);
        machineDao.create(machine);

        RepeatingTrigger trigger = new RepeatingTrigger();
        trigger.setTriggerInterval(5000L);
        triggerDao.create(trigger);

        CommandJob job = new CommandJob();
        List<Trigger> triggers = new ArrayList<Trigger>();
        triggers.add(trigger);

        job.setName("Test Job");
        job.setCommand("java -version");
        job.setStderr("stderr.log");
        job.setStdout("stdout.log");
        job.setAppendStderr(true);
        job.setExecutingServer(machine);
        job.setTriggers(triggers);

        jobDao.create(job);

        trigger.setJob(job);
        triggerDao.create(trigger);

        TimedTrigger timedTrigger = new TimedTrigger();
        LocalTime triggerTime = new LocalTime();
        triggerTime.plusMillis(6000);
        timedTrigger.setTriggerTime(triggerTime);
        timedTrigger.setDaysOfWeek(127);
        triggerDao.create(timedTrigger);

        List<Trigger> timedTriggers = new ArrayList<Trigger>();
        timedTriggers.add(timedTrigger);

        CommandJob job2 = new CommandJob();

        job2.setName("Test Job2");
        job2.setCommand("java -version");
        job2.setStderr("stderr.log");
        job2.setStdout("stdout.log");
        job2.setAppendStderr(true);
        job2.setExecutingServer(machine);
        job2.setTriggers(timedTriggers);
        jobDao.create(job2);

        timedTrigger.setJob(job2);
        triggerDao.create(timedTrigger);

        Job readJob = jobDao.readByName("Test Job");
        log.info("Record read: " + readJob);

//        readJob = jobDao.readById(9999L);
//        log.info("Record read: " + readJob);
//
        List<Job> jobs = jobDao.read();
        for (Job job1 : jobs) {
            log.info("All jobs read: " + job1);
        }
    }
}
