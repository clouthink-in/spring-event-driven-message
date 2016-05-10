package in.clouthink.daas.edm;

/**
 */
public interface EdmRegistry {

    /**
     *
     * @param eventName
     * @param eventListener
     */
    void register(String eventName, Object eventListener);

    /**
     *
     * @param eventName
     */
    void unregister(String eventName);
    
}
