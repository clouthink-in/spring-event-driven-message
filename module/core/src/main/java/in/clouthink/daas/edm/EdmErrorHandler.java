package in.clouthink.daas.edm;

/**
 */
public interface EdmErrorHandler {
    
    /**
     * Handles exceptions thrown by event listener.
     */
    void handleException(Throwable exception, EdmError context);
    
}
