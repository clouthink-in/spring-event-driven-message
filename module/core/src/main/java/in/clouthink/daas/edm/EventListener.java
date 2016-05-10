package in.clouthink.daas.edm;

public interface EventListener<T> {
    
    /**
     * @param event
     */
    void onEvent(T event);
    
}
