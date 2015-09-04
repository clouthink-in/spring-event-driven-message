package in.clouthink.daas.edm.sms.impl;

import java.util.List;

/**
 */
public interface HttpCookieCallback {
    
    /**
     * The acceptable cookie names (the others will be ignored)
     * 
     * @return
     */
    public String[] acceptedCookieNames();
    
    /**
     * All the http cookies will be parsed and saved (which matched the accepted
     * cookie names)
     * 
     * @param cookies
     */
    public void saveCookie(List<String> cookies);
    
    /**
     * All the cookies will convert to the standard http cookie format in one
     * string
     * 
     * @return
     */
    public String getCookie();
    
}
