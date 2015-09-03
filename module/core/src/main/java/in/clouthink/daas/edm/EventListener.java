package in.clouthink.daas.edm;

public interface EventListener<T> {
    
    /**
     * @param event
     */
    public void onEvent(T event);
    
}
