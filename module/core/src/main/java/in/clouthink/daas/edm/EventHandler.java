package in.clouthink.daas.edm;

/**
 */
public interface EventHandler<T> {
    
    public void handle(T evtObj);
    
}
