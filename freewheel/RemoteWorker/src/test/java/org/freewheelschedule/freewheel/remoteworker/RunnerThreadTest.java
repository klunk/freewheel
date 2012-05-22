/*
 * Copyright (c) 2012 SixRQ Ltd.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.freewheelschedule.freewheel.remoteworker;

import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

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
	public void commandReturnedFromPoll() throws InterruptedException {
		final JobInitiationMessage command = new JobInitiationMessage();
		command.setJobType(JobType.COMMAND);
		
			context.checking(new Expectations() {{
				oneOf (jobQueue).poll(timeout, TimeUnit.MILLISECONDS); will(returnValue(command));
				oneOf (threadPool).submit(with(any(Execution.class)));
			}});
		runner.run();
	}
}
