package org.freewheelschedule.freewheel.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.model.Trigger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class TriggerDao extends DefaultDao {
    final static Log log = LogFactory.getLog(TriggerDao.class);

    private final String readByIdQuery = "from Trigger where uid = ";
    private final String readQuery = "from Trigger";

    public Long create(Trigger trigger) {
        Session session = getSession();

        try {
            session.saveOrUpdate(trigger);
            session.flush();
        } catch (HibernateException e) {
            log.error("Error saving Trigger information", e);
        } finally {
            close(session);
        }

        return trigger.getUid();
    }

    public Trigger readById(Long uid) {
        Session session = getSession();
        Trigger result = null;

        try {
            Query query = session.createQuery(readByIdQuery + uid);
            result = (Trigger) query.uniqueResult();
        } finally {
            close(session);
        }

        return result;
    }

    public List<Trigger> read() {
        Session session = getSession();
        List<Trigger> result = null;

        try {
            Query query = session.createQuery(readQuery);
            result = (List<Trigger>) query.list();
        } finally {
            close(session);
        }

        return result;
    }
}