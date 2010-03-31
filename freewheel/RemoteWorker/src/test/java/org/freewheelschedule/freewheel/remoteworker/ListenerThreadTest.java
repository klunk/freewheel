package org.freewheelschedule.freewheel.remoteworker;

import com.google.gson.Gson;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.network.FreewheelSocket;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.freewheelschedule.freewheel.common.message.JobType.COMMAND;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@RunWith(JMock.class)
public class ListenerThreadTest {

    private Gson gson = new Gson();

    Mockery context = new JUnit4Mockery();
    FreewheelSocket inboundSocket;
    BlockingQueue<JobInitiationMessage> jobQueue;

    boolean continueWaiting = false;
    ListenerThread thread;

    @Before
	public void setUp() {
		inboundSocket = context.mock(FreewheelSocket.class);
		jobQueue = new LinkedBlockingQueue<JobInitiationMessage>();
		thread = new ListenerThread();
		thread.setContinueWaiting(continueWaiting);
		thread.setInboundSocket(inboundSocket);
		thread.setJobQueue(jobQueue);
	}
	
	@Test
	public void acceptTimeoutTest() {
		try {
			context.checking(new Expectations() {{
				oneOf (inboundSocket).getPort(); will(returnValue(123));
				oneOf (inboundSocket).waitSocket(); will(throwException(new SocketTimeoutException()));
				oneOf (inboundSocket).close();
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		thread.run();
		
		assertEquals("Expected no jobs in the queue", 0, jobQueue.size());
	}
	
	@Test
	public void validConversationTest() throws InterruptedException {
        final JobInitiationMessage jim = new JobInitiationMessage();
        jim.setCommand("Some command");
        jim.setJobType(COMMAND);

        try {
            context.checking(new Expectations() {{
                oneOf(inboundSocket).getPort();
                will(returnValue(123));
                oneOf(inboundSocket).waitSocket();
                exactly(2).of(inboundSocket).getRemoteMachineName();
                will(returnValue("Dummy machine"));
                oneOf(inboundSocket).close();
                oneOf(inboundSocket).writeSocket("HELO\r\n");
                oneOf(inboundSocket).readSocket();
                will(returnValue("HELO Dummy machine"));
                oneOf(inboundSocket).writeSocket("Enter command to run\r\n");
                oneOf(inboundSocket).readSocket();
                will(returnValue(gson.toJson(jim)));
                oneOf(inboundSocket).writeSocket("Job queued\r\n");
            }});
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        thread.run();

        assertThat(jobQueue.size(), is(equalTo(1)));
//        assertThat(jobQueue.take(), is(equalTo(jim)));

    }
	
	@Test
	public void invalidconversationTest() {
		
		try {
			context.checking(new Expectations() {{
				oneOf         (inboundSocket).getPort(); will(returnValue(123));
				oneOf         (inboundSocket).waitSocket();
				exactly(2).of (inboundSocket).getRemoteMachineName(); will(returnValue("Dummy machine"));
				oneOf         (inboundSocket).close();
				oneOf         (inboundSocket).writeSocket("HELO\r\n");
				oneOf         (inboundSocket).readSocket(); will(returnValue("HELO failure"));
			}});
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		thread.run();
		
		assertThat(jobQueue.size(), is(equalTo(0)));

	}
}
