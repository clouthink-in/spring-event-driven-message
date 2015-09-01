package in.clouthink.daas.edm;

/**
 * Created by Zhang,ChenXUe on 2015/9/1.
 */
public interface EdmRegistry {
    
    public void register(String eventName, Object eventListener);
    
    public void unregister(String eventName);
    
}
