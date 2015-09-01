package in.clouthink.daas.edm;

import java.lang.reflect.Method;

/**
 */
public class EdmError {
    
    private final Edm edm;
    
    private final Object event;
    
    private final Object listener;
    
    private final Method listenableMethod;
    
    /**
     * @param edm
     *            The {@link Edm} that handled the event and the subscriber.
     *            Useful for broadcasting a a new event based on the error.
     * @param event
     *            The event object that caused the subscriber to throw.
     * @param listener
     *            The source subscriber context.
     * @param listenableMethod
     *            the subscribed method.
     */
    EdmError(Edm edm, Object event, Object listener, Method listenableMethod) {
        this.edm = checkNotNull(edm, null);
        this.event = checkNotNull(event, null);
        this.listener = checkNotNull(listener, null);
        this.listenableMethod = checkNotNull(listenableMethod, null);
    }
    
    /**
     * @return The {@link Edm} that handled the event and the subscriber. Useful
     *         for broadcasting a a new event based on the error.
     */
    public Edm getEdm() {
        return edm;
    }
    
    /**
     * @return The event object that caused the subscriber to throw.
     */
    public Object getEvent() {
        return event;
    }
    
    /**
     * @return The object context that the subscriber was called on.
     */
    public Object getListener() {
        return listener;
    }
    
    /**
     * @return The subscribed method that threw the exception.
     */
    public Method getListenableMethod() {
        return listenableMethod;
    }
    
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
    
}
