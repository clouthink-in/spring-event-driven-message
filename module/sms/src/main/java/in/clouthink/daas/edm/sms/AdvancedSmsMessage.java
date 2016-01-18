package in.clouthink.daas.edm.sms;

public class AdvancedSmsMessage<T> {
    
    private String cellphone;
    
    private String message;
    
    private T options;
    
    public String getCellphone() {
        return cellphone;
    }
    
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getOptions() {
        return options;
    }
    
    public void setOptions(T options) {
        this.options = options;
    }
    
}
