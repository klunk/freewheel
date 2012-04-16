/*
 * Copyright 2012 Copyright SixRQ Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
