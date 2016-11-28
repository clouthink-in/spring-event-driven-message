package in.clouthink.daas.edm.push;

/**
 * The push message sender
 */
public interface PushSender {
    
    /**
     * Push the message
     *
     * @param pushMessage
     */
    void push(PushMessage pushMessage);
    
    /**
     * Push the message and handle the response
     *
     * @param pushMessage
     * @param responseReceiver
     * @since 1.0.1
     */
    void push(PushMessage pushMessage, PushResponseReceiver responseReceiver);
    
}
