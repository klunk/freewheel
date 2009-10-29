package org.freewheelschedule.freewheel.remoteworker;

import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;

public interface Execution extends Runnable {

	void setCommand(JobInitiationMessage command);

}
