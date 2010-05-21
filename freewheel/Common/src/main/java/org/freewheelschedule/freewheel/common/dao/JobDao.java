package org.freewheelschedule.freewheel.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.model.Job;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class JobDao {
    private final static Log log = LogFactory.getLog(JobDao.class);

    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Long create(Job job) {
        Session session = getSession();

        try {
            session.saveOrUpdate(job);
            session.flush();
        } catch (HibernateException e) {
            log.error("Error saving Job information", e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    log.error("Failed to cleanly close the session", e);
                }
            }
        }
            
        return job.getUid();
    }

    private org.hibernate.classic.Session getSession() {
        return sessionFactory.openSession();
    }
}
