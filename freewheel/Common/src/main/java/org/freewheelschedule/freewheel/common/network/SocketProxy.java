package org.freewheelschedule.freewheel.common.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketProxy implements ISocketProxy {
	
	Socket socket;
	PrintWriter writer = null;
	BufferedReader reader = null;
	
	public SocketProxy(Socket socket) throws IOException {
		this.socket = socket;
		writer = new PrintWriter(new OutputStreamWriter(((Socket)socket).getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(((Socket)socket).getInputStream()));
	}

	@Override
	public void write(String message) {
		writer.write(message);
		writer.flush();
	}

	@Override
	public String read() throws IOException {
		return reader.readLine();
	}

}
