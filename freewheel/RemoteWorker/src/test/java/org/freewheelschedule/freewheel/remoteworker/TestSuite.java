package org.freewheelschedule.freewheel.remoteworker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ListenerThreadTest.class,
		RunnerThreadTest.class
})
public class TestSuite {

}
