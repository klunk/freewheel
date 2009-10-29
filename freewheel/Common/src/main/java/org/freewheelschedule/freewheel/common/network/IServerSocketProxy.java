package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;
import java.net.SocketTimeoutException;

public interface IServerSocketProxy {

	public ISocketProxy accept() throws IOException, SocketTimeoutException;
	public void close() throws IOException;
	
}
