/*
 * Copyright (c) 2010. This file is copyright SJW Computer Consultancy Ltd. The code may be modified as per the GNU Public Licence.
 */

package org.freewheelschedule.freewheel.common.network;

import java.io.*;
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

	@Override
	public void close() throws IOException {
		socket.close();
		writer.close();
		reader.close();
	}

	@Override
	public String getRemoteMachineName() {
		return socket.getInetAddress().getCanonicalHostName();
	}

}
