package in.clouthink.daas.edm;

/**
 */
public interface Edm extends EdmRegistry {
    
    /**
     * @param eventName
     * @param eventObject
     * @param <T>
     */
    <T> void dispatch(String eventName, T eventObject);
    
}
