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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(JMock.class)
public class FreewheelSocketTest {

	Mockery context = new JUnit4Mockery();
	FreewheelSocket fwSocket;

	IServerSocketProxy serverSocket = null;
	ISocketProxy socket = null;

	final String message = "Test message to socket";

	@Before
	public void setUp() {
		fwSocket = new FreewheelClientSocket();
		serverSocket = context.mock(IServerSocketProxy.class);
		socket = context.mock(ISocketProxy.class);
		((FreewheelClientSocket)fwSocket).setSocket(serverSocket);
	}
	
	@Test
	public void waitTest() {

		try {
			context.checking(new Expectations() {{
				oneOf (serverSocket).accept(); will(returnValue(socket));
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			fwSocket.waitSocket();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test 
	public void writeTest() {
		
		//setup test
		try {
			context.checking(new Expectations() {{
				oneOf (serverSocket).accept(); will(returnValue(socket));
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			fwSocket.waitSocket();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		// test functionality
		context.checking(new Expectations() {{
			oneOf (socket).write(message);
		}});
		
		fwSocket.writeSocket(message);
		
	}
	
	@Test
	public void readTest() {
		
		//setup test
		try {
			context.checking(new Expectations() {{
				oneOf (serverSocket).accept(); will(returnValue(socket));
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			fwSocket.waitSocket();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		// test functionality
		try {
			context.checking(new Expectations() {{
				oneOf (socket).read(); will(returnValue(message));
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		String result = null;
		try {
			result = fwSocket.readSocket();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		assertThat(result, is(equalTo(message)));
	}
	
	@Test
	public void closeTest() {
		//setup test
		try {
			context.checking(new Expectations() {{
				oneOf (serverSocket).accept(); will(returnValue(socket));
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			fwSocket.waitSocket();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			context.checking(new Expectations() {{
				oneOf (socket).close();
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			fwSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}
}
