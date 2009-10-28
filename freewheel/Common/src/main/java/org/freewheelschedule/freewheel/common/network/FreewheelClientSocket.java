package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;

import lombok.Setter;

public class FreewheelClientSocket implements FreewheelSocket {

	private @Setter IServerSocketProxy socket;
	private ISocketProxy server;
	
	@Override
	public void waitSocket() throws IOException {
		if (server == null) {
			server = socket.accept();
		}
	}
	
	@Override
	public void writeSocket(String message) {
		server.write(message);
		
	}
	
	@Override
	public String readSocket() throws IOException {
		return server.read();
	}
}
