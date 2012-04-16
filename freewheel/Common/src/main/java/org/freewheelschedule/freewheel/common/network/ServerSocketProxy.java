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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class ServerSocketProxy implements IServerSocketProxy {

	ServerSocket serverSocket;
    int port;
	
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


    public int getPort() {
        return port;
    }
}
