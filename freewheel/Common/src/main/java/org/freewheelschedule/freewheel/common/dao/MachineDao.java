package org.freewheelschedule.freewheel.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.model.Machine;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class MachineDao extends DefaultDao {
        final static Log log = LogFactory.getLog(DefaultDao.class);

    private final String readQuery = "from Machine where uid = ";

    public Long create(Machine machine) {
        Session session = getSession();

        try {
            session.saveOrUpdate(machine);
            session.flush();
        } catch (HibernateException e) {
            log.error("Error saving Machine information", e);
        } finally {
            close(session);
        }

        return machine.getUid();
    }

    public Machine read(Long uid) {
        Session session = getSession();
        Machine result = null;

        try {
            Query query = session.createQuery(readQuery + uid);
            result = (Machine) query.uniqueResult();
        } finally {
            close(session);
        }

        return result;
    }
}
