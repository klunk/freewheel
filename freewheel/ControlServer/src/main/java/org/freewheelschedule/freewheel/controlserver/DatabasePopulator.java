package org.freewheelschedule.freewheel.controlserver;

import org.freewheelschedule.freewheel.common.dao.JobDao;
import org.freewheelschedule.freewheel.common.model.CommandJob;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class DatabasePopulator {

    @Transactional
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-ControlServer.xml");
        JobDao jobDao = (JobDao) ctx.getBean("jobDao");

        CommandJob job = new CommandJob();

        job.setName("First Job");
        job.setCommand("java -version");
        job.setStderr("stderr.log");
        job.setStdout("stdout.log");

        jobDao.create(job);
    }
}
