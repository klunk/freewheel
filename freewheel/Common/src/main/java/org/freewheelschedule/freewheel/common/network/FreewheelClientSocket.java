/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;

public class FreewheelClientSocket implements FreewheelSocket {

	private IServerSocketProxy socket;
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

	@Override
	public void close() throws IOException {
		if (server != null) {
			server.close();
			server = null;
		}
	}
	
	@Override
	public String getRemoteMachineName() {
		return server.getRemoteMachineName();
	}
	
	@Override
	public int getPort() {
		return ((ServerSocketProxy)socket).getPort();
	}

    public void setSocket(IServerSocketProxy serverSocket) {
        this.socket = serverSocket;
    }
}
