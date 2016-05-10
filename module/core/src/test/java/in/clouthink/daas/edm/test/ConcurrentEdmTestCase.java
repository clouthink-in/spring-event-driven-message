package in.clouthink.daas.edm.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import in.clouthink.daas.edm.Edms;

@RunWith(MockitoJUnitRunner.class)
public class ConcurrentEdmTestCase {
    
    @Before
    public void setUp() throws Exception {
    
    }
    
    @After
    public void tearDown() throws Exception {
    
    }
    
    @Test
    public void testStandardEventListener() throws InterruptedException {
        Edms.getEdm("test1").register("StandardEventListener",
                                      new SleepEventListener(8 * 1000));
        Edms.getEdm("test2").register("StandardEventListener",
                                      new SleepEventListener(1 * 1000));
        Edms.getEdm("test2").register("StandardEventListener",
                                      new SleepEventListener(2 * 1000));
        Edms.getEdm("test2").register("StandardEventListener",
                                      new SleepEventListener(7 * 1000));
        Edms.getEdm("test1").dispatch("StandardEventListener", "hello 1");
        Edms.getEdm("test2").dispatch("StandardEventListener", "hello 2");
        
        Thread.sleep(15 * 1000);
    }
    
}
