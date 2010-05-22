package org.freewheelschedule.freewheel.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.model.Job;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class JobDao extends DefaultDao {
    final static Log log = LogFactory.getLog(JobDao.class);

    private final String readByIdQuery = "from Job where uid = ";
    private final String readByNameQuery = "from Job where name = ";
    private final String readAllQuery = "from Job";

    public Long create(Job job) {
        Session session = getSession();

        try {
            session.saveOrUpdate(job);
            session.flush();
        } catch (HibernateException e) {
            log.error("Error saving Job information", e);
        } finally {
            close(session);
        }
            
        return job.getUid();
    }

    public Job readById(Long uid) {
        Session session = getSession();
        Job result = null;

        try {
            Query query = session.createQuery(readByIdQuery + uid);
            result = (Job) query.uniqueResult();
        } catch (HibernateException e) {
            log.error("Error saving Job information", e);
        } finally {
            close(session);
        }

        return result;
    }

    public Job readByName(String name) {
        Session session = getSession();
        Job result = null;

        try {
            Query query = session.createQuery(readByNameQuery +  "'" + name + "'");
            result = (Job) query.uniqueResult();
        } catch (HibernateException e) {
            log.error("Error saving Job information", e);
        } finally {
            close(session);
        }

        return result;
    }

    public List<Job> read() {
        Session session = getSession();
        List<Job> result = null;

        try {
            Query query = session.createQuery(readAllQuery);
            result = (List<Job>) query.list();
        } catch (HibernateException e) {
            log.error("Error saving Job information", e);
        } finally {
            close(session);
        }
        return result;
    }
}
