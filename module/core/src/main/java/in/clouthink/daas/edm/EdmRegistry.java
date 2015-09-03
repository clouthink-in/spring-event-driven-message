package in.clouthink.daas.edm;

/**
 */
public interface EdmRegistry {
    
    public void register(String eventName, Object eventListener);
    
    public void unregister(String eventName);
    
}
