package in.clouthink.daas.edm.sms;

public interface SmsSender {
    
    void send(SmsMessage smsMessage);
    
    <T> void send(AdvancedSmsMessage<T> smsMessage);
    
}
