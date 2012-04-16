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
            result.getExecutions().size();
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

    public void loadLazyCollections(Job job) {
        Session session = getSession();
        session.refresh(job);
    }
}
