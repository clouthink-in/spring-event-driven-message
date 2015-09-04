package in.clouthink.daas.edm.push.impl;

public class JPushOptions {
    
    private String appKey;
    
    private String appSecret;
    
    /**
     * how many times to try jpush rest api
     *
     * @return
     */
    private int maxRetries;
    
    /**
     * keep the time for offline message , the unit is second，the max value is
     * ten days
     *
     * @return
     */
    private long timeToLive;
    
    private boolean apnsProduction;
    
    public String getAppKey() {
        return appKey;
    }
    
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    
    public int getMaxRetries() {
        return maxRetries;
    }
    
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    public long getTimeToLive() {
        return timeToLive;
    }
    
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
    
    public boolean isApnsProduction() {
        return apnsProduction;
    }
    
    public void setApnsProduction(boolean apnsProduction) {
        this.apnsProduction = apnsProduction;
    }
}
