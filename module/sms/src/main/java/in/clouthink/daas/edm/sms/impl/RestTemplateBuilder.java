package in.clouthink.daas.edm.sms.impl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 */
public class RestTemplateBuilder {
    
    private String encoding = "UTF-8";
    
    private boolean simulateBrowser = false;
    
    private boolean nativeCookieEnabled = false;
    
    private boolean hookCookieEnabled = false;
    
    private HttpCookieCallback httpCookieCallback;
    
    private boolean sslEnabled = false;
    
    private int sslPort = 443;
    
    public RestTemplateBuilder simulateBrowser() {
        this.simulateBrowser = true;
        return this;
    }
    
    public RestTemplateBuilder enableNativeCookie() {
        this.nativeCookieEnabled = true;
        this.hookCookieEnabled = false;
        return this;
    }
    
    public RestTemplateBuilder enableHookCookie(HttpCookieCallback httpCookieCallback) {
        this.nativeCookieEnabled = false;
        this.hookCookieEnabled = true;
        if (httpCookieCallback == null) {
            throw new NullPointerException("The httpCookieCallback is required.");
        }
        this.httpCookieCallback = httpCookieCallback;
        return this;
    }
    
    public RestTemplateBuilder enableSsl(int port) {
        this.sslEnabled = true;
        this.sslPort = port;
        return this;
    }
    
    private HttpClient createHttpClient() {
        // for http client 4.3 , it should be CloseableHttpClient client =
        // HttpClients.createDefault();
        DefaultHttpClient client = new DefaultHttpClient();
        if (sslEnabled) {
            try {
                TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] certificate,
                                             String authType) {
                        return true;
                    }
                };
                SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy,
                                                           SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                                                           
                client.getConnectionManager()
                      .getSchemeRegistry()
                      .register(new Scheme("https", sslPort, sf));
            }
            catch (Exception e) {
                throw new RuntimeException("Enable ssl feature for http client failed.",
                                           e);
            }
        }
        
        if (nativeCookieEnabled) {
            HttpClientParams.setCookiePolicy(client.getParams(),
                                             CookiePolicy.BROWSER_COMPATIBILITY);
            BasicCookieStore cookieStore = new BasicCookieStore();
            client.setCookieStore(cookieStore);
        }
        
        if (simulateBrowser) {
            client.getParams()
                  .setParameter(CoreProtocolPNames.USER_AGENT,
                          "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        }
        
        client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
                encoding);
                                        
        return client;
    }
    
    public RestTemplate build() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(createHttpClient());
        RestTemplate result = new RestTemplate(clientHttpRequestFactory);
        
//        result.setMessageConverters(getJackson2MessageConverters());
        
        if (hookCookieEnabled) {
            result.getInterceptors().add(getClientHttpRequestInterceptor());
        }
        
        return result;
    }
    
    private List<HttpMessageConverter<?>> getJackson2MessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        return converters;
    }
    
    private ClientHttpRequestInterceptor getClientHttpRequestInterceptor() {
        return new ClientHttpRequestInterceptor() {
            
            @Override
            public ClientHttpResponse intercept(HttpRequest request,
                                                byte[] body,
                                                ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().set("Cookie",
                                         httpCookieCallback.getCookie());
                return execution.execute(request, body);
            }
            
        };
    }
}
