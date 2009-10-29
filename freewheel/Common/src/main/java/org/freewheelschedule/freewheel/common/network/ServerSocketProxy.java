package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;
import java.net.ServerSocket;

import lombok.Getter;

public class ServerSocketProxy implements IServerSocketProxy {

	ServerSocket serverSocket;
	@Getter int port;
	
	public ServerSocketProxy() throws IOException {
		serverSocket = new ServerSocket();
	}

	public ServerSocketProxy(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
	}

	@Override
	public ISocketProxy accept() throws IOException {
		return new SocketProxy(serverSocket.accept());
	}

	@Override
	public void close() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
		
	}

}
