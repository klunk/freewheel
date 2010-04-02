/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.remoteworker;

import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;

public interface Execution extends Runnable {

	void setCommand(JobInitiationMessage command);

}
