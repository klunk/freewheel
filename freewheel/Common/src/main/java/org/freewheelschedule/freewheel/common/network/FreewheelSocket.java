package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;

public interface FreewheelSocket {

	public void waitSocket() throws IOException;
	public void writeSocket(String message);
	public String readSocket() throws IOException;
	public void close() throws IOException;
	public String getRemoteMachineName();
	public int getPort();
	
}