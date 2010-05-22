package org.freewheelschedule.freewheel.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.model.Trigger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class TriggerDao extends DefaultDao {
    final static Log log = LogFactory.getLog(TriggerDao.class);

    private final String readQuery = "from Trigger where uid = ";

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

    public Trigger read(Long uid) {
        Session session = getSession();
        Trigger result = null;

        try {
            Query query = session.createQuery(readQuery + uid);
            result = (Trigger) query.uniqueResult();
        } finally {
            close(session);
        }

        return result;
    }
}