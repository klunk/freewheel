package org.freewheelschedule.freewheel.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class DefaultDao {
    final static Log log = LogFactory.getLog(DefaultDao.class);
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException e) {
                log.error("Failed to cleanly close the session", e);
            }
        }
    }

    protected Session getSession() {
        return sessionFactory.openSession();
    }
}