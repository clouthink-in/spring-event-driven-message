package in.clouthink.daas.edm.push;

/**
 * @since 1.0.1
 */
public interface PushResponseReceiver {
    
    /**
     * Receive and handle the push response, any exception thrown will be
     * ignored.
     *
     * @param response
     */
    void onResponse(PushResponse response);
    
}
