package in.clouthink.daas.edm.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import in.clouthink.daas.edm.Edms;

@RunWith(MockitoJUnitRunner.class)
public class NamedEdmTestCase {
    
    @Before
    public void setUp() throws Exception {
    
    }
    
    @After
    public void tearDown() throws Exception {
    
    }
    
    @Test
    public void testStandardEventListener() {
        Edms.getEdm("test1").register("StandardEventListener",
                               new StandardEventListener());
        Edms.getEdm("test1").dispatch("StandardEventListener", "hello");
    }
    
    @Test
    public void testAnnotatedEventListener() {
        Edms.getEdm("test2").register("AnnotatedEventListener",
                               new AnnotatedEventListener());
        Edms.getEdm("test2").dispatch("AnnotatedEventListener", "hello");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAnnotatedEventListener1() {
        Edms.getEdm("test3").register("InvalidAnnotatedEventListener1",
                               new InvalidAnnotatedEventListener1());
        Edms.getEdm("test3").dispatch("InvalidAnnotatedEventListener1", "hello");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAnnotatedEventListener2() {
        Edms.getEdm("test4").register("InvalidAnnotatedEventListener2",
                               new InvalidAnnotatedEventListener2());
        Edms.getEdm("test4").dispatch("InvalidAnnotatedEventListener2", "hello");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAnnotatedEventListener3() {
        Edms.getEdm("test5").register("InvalidAnnotatedEventListener3",
                               new InvalidAnnotatedEventListener3());
        Edms.getEdm("test5").dispatch("InvalidAnnotatedEventListener3", "hello");
    }
    
}
