package in.clouthink.daas.edm.sms;

public class SmsMessage {
    
    private String cellphone;
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getCellphone() {
        return cellphone;
    }
    
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
    
    private String message;
}
