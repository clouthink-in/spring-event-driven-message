package in.clouthink.daas.edm.reactor;

import static reactor.bus.selector.Selectors.$;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;
import in.clouthink.daas.edm.Edm;
import in.clouthink.daas.edm.EventListener;
import in.clouthink.daas.edm.Listenable;

/**
 */
public class EdmImpl implements Edm {
    
    private static final Log logger = LogFactory.getLog(EdmImpl.class);
    
    public static class DefaultConsumerAdatper implements Consumer<Event> {
        
        private EventListener eventEventListener;
        
        public DefaultConsumerAdatper(EventListener eventEventListener) {
            this.eventEventListener = eventEventListener;
        }
        
        public void accept(Event t) {
            eventEventListener.onEvent(t.getData());
        }
        
    }
    
    public static class ReflectedConsumerAdatper implements Consumer<Event> {
        
        private Object target;
        
        private Method method;
        
        public ReflectedConsumerAdatper(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
        
        public void accept(Event t) {
            try {
                method.invoke(target, t.getData());
            }
            catch (IllegalArgumentException e) {
                throw new Error("Method rejected target/argument: "
                                + t.getData(), e);
            }
            catch (IllegalAccessException e) {
                throw new Error("Method became inaccessible: " + t.getData(),
                                e);
            }
            catch (InvocationTargetException e) {
                if (e.getCause() instanceof Error) {
                    throw (Error) e.getCause();
                }
                throw new RuntimeException(e);
            }
        }
        
    }
    
    private EventBus r;
    
    public EdmImpl() {
        Environment env = new Environment();
        r = EventBus.config().env(env).dispatcher(Environment.SHARED).get();
    }
    
    @Override
    public void register(String eventName, Object eventHandler) {
        if (eventHandler == null) {
            throw new NullPointerException();
        }
        if (eventHandler instanceof EventListener) {
            r.<Event> on($(eventName),
                         new DefaultConsumerAdatper((EventListener) eventHandler));
            return;
        }
        
        Method method = resolveMethod(eventHandler);
        r.<Event> on($(eventName),
                     new ReflectedConsumerAdatper(eventHandler, method));
    }
    
    private Method resolveMethod(Object listener) {
        Class clazz = listener.getClass();
        
        Method result = null;
        short howManyMatched = 0;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Listenable.class)
                && !method.isSynthetic()) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException(String.format("Method '%s' with @Listenable annotation has %s parameters. Listenable methods must have exactly 1 parameter.",
                                                                     method,
                                                                     parameterTypes.length));
                }
                result = method;
                howManyMatched++;
            }
        }
        if (howManyMatched == 0) {
            throw new IllegalArgumentException("No method with @Listenable annotation found.");
        }
        
        if (howManyMatched > 1) {
            throw new IllegalArgumentException(String.format("%s methods with @Listenable annotation found. So far we support only one listenable method exactly. ",
                                                             howManyMatched));
        }
        
        return result;
    }
    
    @Override
    public void unregister(String eventName) {
        // TODO
    }
    
    @Override
    public <T> void dispatch(String eventName, T eventObject) {
        r.notify(eventName, Event.wrap(eventObject));
    }
    
}
