package in.clouthink.daas.edm.sms.impl;

import java.util.HashMap;
import java.util.List;

/**
 */
public class BaseHttpCookieCallback implements HttpCookieCallback {
    
    private String[] acceptedCookieNames;
    
    private HashMap<String, String> availableCookies = new HashMap<String, String>();
    
    public BaseHttpCookieCallback(String acceptedCookieName) {
        this.acceptedCookieNames = new String[] { acceptedCookieName };
    }
    
    public BaseHttpCookieCallback(String[] acceptedCookieNames) {
        this.acceptedCookieNames = acceptedCookieNames;
    }
    
    @Override
    public String[] acceptedCookieNames() {
        return this.acceptedCookieNames;
    }
    
    @Override
    public void saveCookie(List<String> cookies) {
        if (cookies != null) {
            for (String rawCookie : cookies) {
                for (String thisCookieName : acceptedCookieNames) {
                    if (rawCookie.startsWith(thisCookieName)) {
                        int valueEnd = rawCookie.indexOf(';');
                        if (valueEnd == -1) {
                            valueEnd = rawCookie.length();
                        }
                        availableCookies.put(thisCookieName,
                                             rawCookie.substring((rawCookie.indexOf("=") + 1),
                                                                 valueEnd));
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public String getCookie() {
        StringBuilder cookiesValue = new StringBuilder();
        
        for (String cookieName : acceptedCookieNames) {
            String cookieValue = availableCookies.get(cookieName);
            if (cookiesValue != null) {
                cookiesValue.append(String.format("%s=%s; ",
                        cookieName,
                        cookieValue));
            }
        }
        
        return cookiesValue.toString();
    }
}
