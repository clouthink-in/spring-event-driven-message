package in.clouthink.daas.edm.sms.impl;

public class ZhiyanOptions {
    
    String apiKey;
    
    String token;
    
    String templateId;
    
    String url;
    
    public ZhiyanOptions() {
    }
    
    public ZhiyanOptions(String apiKey,
                         String token,
                         String templateId,
                         String url) {
        this.apiKey = apiKey;
        this.token = token;
        this.templateId = templateId;
        this.url = url;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
