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

import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.common.message.JobInitiationMessage;
import org.freewheelschedule.freewheel.common.message.JobType;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RunnerThread implements Runnable, Runner, InitializingBean {

    private static Log log = LogFactory.getLog(RunnerThread.class);

    private
    @Setter
    int numberOfThreads;
    private
    @Setter
    BlockingQueue<JobInitiationMessage> jobQueue;
    private
    @Setter
    ExecutorService threadPool;
    private
    @Setter
    boolean continueWaiting = true;
    private
    @Setter
    int timeout;
    private int remotePort;

    public RunnerThread() {
        super();
    }

    public RunnerThread(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        createThreadPool();
    }

    @Override
    public void run() {
        log.info("Worker thread is running.");

        Execution commandLine = null;

        do {
            try {
                log.debug("Waiting for command to come from listener.");
                JobInitiationMessage command = jobQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (command != null) {
                    log.info("Command received: " + command);
                    if (command.getJobType().equals(JobType.COMMAND)) {
                        commandLine = new CommandLineExecution();
                        commandLine.setCommand(command);
                        commandLine.setRemotePort(remotePort);
                    }
                    threadPool.submit(commandLine);
                } else {
                    log.info("Timeout waiting for jobQueue");
                }
            } catch (InterruptedException e) {
                log.error("RunnerThread sleep was interrupted", e);
            }
        } while (continueWaiting);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (threadPool == null) {
            createThreadPool();
        }
    }

    private void createThreadPool() {
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void stopExecutions() {
        threadPool.shutdownNow();
        continueWaiting = false;
    }

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }

    public void setJobQueue(BlockingQueue<JobInitiationMessage> jobQueue) {
        this.jobQueue = jobQueue;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
}
