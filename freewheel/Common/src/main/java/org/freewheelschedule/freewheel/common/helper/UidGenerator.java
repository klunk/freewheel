/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.common.helper;

public class UidGenerator {
    private static Long uid = new Long(0);

    public static synchronized Long getNewUid() {
        return ++uid;
    }
}
