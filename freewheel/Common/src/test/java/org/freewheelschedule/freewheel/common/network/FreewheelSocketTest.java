package org.freewheelschedule.freewheel.common.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class FreewheelSocketTest {

	Mockery context = new JUnit4Mockery();
	FreewheelSocket fwSocket;

	IServerSocketProxy serverSocket = null;
	ISocketProxy socket = null;
	IInputStreamProxy inputStream = null;
	IOutputStreamProxy outputStream = null;

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
		
		assertEquals("Message returned does not match expected result", message, result);
	}
}
