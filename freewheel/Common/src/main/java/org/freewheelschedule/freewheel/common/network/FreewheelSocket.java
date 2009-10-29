package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;
import java.net.SocketTimeoutException;

public interface FreewheelSocket {

	public void waitSocket() throws IOException, SocketTimeoutException;
	public void writeSocket(String message);
	public String readSocket() throws IOException;
	public void close() throws IOException;
	public String getRemoteMachineName();
	public int getPort();
	
}
