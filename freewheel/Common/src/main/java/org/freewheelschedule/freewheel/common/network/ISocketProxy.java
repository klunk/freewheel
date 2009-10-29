package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;


public interface ISocketProxy {

	public void write(String message);

	public String read() throws IOException;

	public void close() throws IOException;
	
}
