package in.clouthink.daas.edm;

/**
 */
public interface EdmErrorHandler {
    
    /**
     * Handles exceptions thrown by event listener.
     */
    void handle(Throwable exception, EdmError context);
    
}
