package org.freewheelschedule.freewheel.controlserver;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.dao.MachineDao;
import org.freewheelschedule.freewheel.common.model.CommandJob;
import org.freewheelschedule.freewheel.common.model.Job;
import org.freewheelschedule.freewheel.common.model.Machine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DatabasePopulator {

    private final static Log log = LogFactory.getLog(DatabasePopulator.class);

    private static Gson gson = new Gson();

    @Transactional
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-ControlServer.xml");
        JobDao jobDao = (JobDao) ctx.getBean("jobDao");
        MachineDao machineDao = (MachineDao) ctx.getBean("machineDao");

        Machine machine = new Machine();
        machine.setName("localhost");
        machine.setPort(12145L);
        machineDao.create(machine);

        CommandJob job = new CommandJob();

        job.setName("Test Job");
        job.setCommand("java -version");
        job.setStderr("stderr.log");
        job.setStdout("stdout.log");
        job.setAppendStderr(true);
        job.setExecutingServer(machine);

        jobDao.create(job);

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
