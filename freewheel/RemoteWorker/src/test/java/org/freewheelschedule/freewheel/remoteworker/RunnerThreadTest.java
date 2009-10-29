package org.freewheelschedule.freewheel.remoteworker;

import static org.junit.Assert.fail;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class RunnerThreadTest {

	Mockery context = new JUnit4Mockery();

	ExecutorService threadPool;
	BlockingQueue<JobInitiationMessage> jobQueue;
	
	boolean continueWaiting = false;
	RunnerThread runner;
	int timeout = 5000;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		jobQueue = context.mock(BlockingQueue.class);
		threadPool = context.mock(ExecutorService.class);
		
		runner = new RunnerThread();
		runner.setContinueWaiting(continueWaiting);
		runner.setJobQueue(jobQueue);
		runner.setThreadPool(threadPool);
		runner.setTimeout(timeout);
	}
	
	@Test
	public void nullReturnedFromPollTest() {
		try {
			context.checking(new Expectations() {{
				oneOf (jobQueue).poll(timeout, TimeUnit.MILLISECONDS); will(returnValue(null));
			}});
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		
		runner.run();
		
	}
	
	@Test
	public void commandReturnedFromPoll() {
		final JobInitiationMessage command = new JobInitiationMessage();
		command.setJobType(JobType.COMMAND);
		
		try {
			context.checking(new Expectations() {{
				oneOf (jobQueue).poll(timeout, TimeUnit.MILLISECONDS); will(returnValue(command));
				oneOf (threadPool).submit(with(any(Execution.class)));
			}});
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		
		runner.run();
	}
}
