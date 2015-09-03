package in.clouthink.daas.edm.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import in.clouthink.daas.edm.Edms;

@RunWith(MockitoJUnitRunner.class)
public class EdmTestCase {
    
    @Before
    public void setUp() throws Exception {
    
    }
    
    @After
    public void tearDown() throws Exception {
    
    }
    
    @Test
    public void testStandardEventListener() {
        Edms.getEdm().register("StandardEventListener",
                               new StandardEventListener());
        Edms.getEdm().dispatch("StandardEventListener", "hello");
    }
    
    @Test
    public void testAnnotatedEventListener() {
        Edms.getEdm().register("AnnotatedEventListener",
                               new AnnotatedEventListener());
        Edms.getEdm().dispatch("AnnotatedEventListener", "hello");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAnnotatedEventListener1() {
        Edms.getEdm().register("InvalidAnnotatedEventListener1",
                               new InvalidAnnotatedEventListener1());
        Edms.getEdm().dispatch("InvalidAnnotatedEventListener1", "hello");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAnnotatedEventListener2() {
        Edms.getEdm().register("InvalidAnnotatedEventListener2",
                               new InvalidAnnotatedEventListener2());
        Edms.getEdm().dispatch("InvalidAnnotatedEventListener2", "hello");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAnnotatedEventListener3() {
        Edms.getEdm().register("InvalidAnnotatedEventListener3",
                               new InvalidAnnotatedEventListener3());
        Edms.getEdm().dispatch("InvalidAnnotatedEventListener3", "hello");
    }
    
}
