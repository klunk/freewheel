package org.freewheelschedule.freewheel.common.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import lombok.Getter;

public class ServerSocketProxy implements IServerSocketProxy {

	ServerSocket serverSocket;
	@Getter int port;
	
	public ServerSocketProxy() throws IOException {
		serverSocket = new ServerSocket();
	}

	public ServerSocketProxy(int port, int timeout) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(timeout);
	}

	@Override
	public ISocketProxy accept() throws IOException, SocketTimeoutException {
		return new SocketProxy(serverSocket.accept());
	}

	@Override
	public void close() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
		
	}

}
