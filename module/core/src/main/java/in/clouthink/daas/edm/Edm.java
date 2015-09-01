package in.clouthink.daas.edm;

/**
 */
public interface Edm extends EdmRegistry {
    
    public <T> void dispatch(String eventName, T eventObject);
    
}
