package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;

public interface IServerSocketProxy {

	public ISocketProxy accept() throws IOException;
	public void close() throws IOException;
	
}
