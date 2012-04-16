/*
 * Copyright 2012 Copyright SixRQ Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
    private final String readByJobIdQuery = "from Trigger where job_uid = ";

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

    public Trigger readByJobId(Long uid) {
        Session session = getSession();
        Trigger result = null;

        try {
            Query query = session.createQuery(readByJobIdQuery + uid);
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