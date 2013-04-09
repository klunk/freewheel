/*
 * Copyright (c) 2013 SixRQ Ltd.
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

package org.freewheelschedule.freewheel.common.util;

import org.freewheelschedule.freewheel.common.model.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JMock.class)
public class QueueWrapperTest {

    Mockery context = new JUnit4Mockery();

    QueueWrapper objectUnderTest = new QueueWrapper();
    private Map<TriggerType, BlockingQueue<Trigger>> queueMap = new HashMap<TriggerType, BlockingQueue<Trigger>>();
    BlockingQueue repeatingQueue = context.mock(BlockingQueue.class, "Repeating");
    BlockingQueue timedQueue = context.mock(BlockingQueue.class, "Timed");

    @Before
    public void setUp() {
        queueMap.put(TriggerType.REPEATING, repeatingQueue);
        queueMap.put(TriggerType.TIMED, timedQueue);
        objectUnderTest.populateQueueMap(queueMap);
    }

    @Test
    public void testPutTimedTrigger() throws Exception {
        final Trigger testTrigger = new TimedTrigger();
        context.checking(new Expectations() {{
            oneOf(timedQueue).put(testTrigger);
        }});
        objectUnderTest.put(testTrigger);
    }

    @Test
    public void testRemoveRepeatingTrigger() throws Exception {
        final Trigger testTrigger = new RepeatingTrigger();
        context.checking((new Expectations() {{
            oneOf(repeatingQueue).remove(testTrigger);
        }}));
        objectUnderTest.remove(testTrigger);
    }

//    @Test
//    public void testGetNextTriggerReturnsNullWhenNoTriggersToFire() throws Exception {
//        final TriggerActions testTimedTrigger = context.mock(TriggerActions.class, "TimeTrigger");
//        final TriggerActions testRepeatingTrigger = context.mock(TriggerActions.class, "RepeatingTrigger");
//        context.checking(new Expectations(){{
//            oneOf(repeatingQueue).peek();
//            will(returnValue(testTimedTrigger));
//            oneOf(timedQueue).peek();
//            will(returnValue(testRepeatingTrigger));
//            oneOf(testTimedTrigger).isTriggered();
//            will(returnValue(false));
//            oneOf(testRepeatingTrigger).isTriggered();
//            will(returnValue(false));
//        }});
//        assertThat(objectUnderTest.getNextTrigger(), is(equalTo(null)));
//    }
}
