package org.freewheelschedule.freewheel.controlserver;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.dao.MachineDao;
import org.freewheelschedule.freewheel.common.dao.TriggerDao;
import org.freewheelschedule.freewheel.common.model.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class DatabasePopulator {

    private final static Log log = LogFactory.getLog(DatabasePopulator.class);

    private static Gson gson = new Gson();

    @Transactional
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-ControlServer.xml");
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
        job = new CommandJob();

        job.setName("Test Job2");
        job.setCommand("java -version");
        job.setStderr("stderr.log");
        job.setStdout("stdout.log");
        job.setAppendStderr(true);
        job.setExecutingServer(machine);

        jobDao.create(job);

        Job readJob = jobDao.readByName("Test Job");
        log.info("Record read: " + readJob);

        readJob = jobDao.readById(9999L);
        log.info("Record read: " + readJob);

        List<Job> jobs = jobDao.read();
        for (Job job1 : jobs) {
            log.info("All jobs read: " + job1);
        }
    }
}
