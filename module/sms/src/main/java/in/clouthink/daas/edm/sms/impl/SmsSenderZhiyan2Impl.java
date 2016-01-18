package in.clouthink.daas.edm.sms.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.clouthink.daas.edm.Listenable;
import in.clouthink.daas.edm.sms.AdvancedSmsMessage;
import in.clouthink.daas.edm.sms.SmsMessage;
import in.clouthink.daas.edm.sms.SmsSender;

public class SmsSenderZhiyan2Impl implements SmsSender {
    
    private static final Log logger = LogFactory.getLog(SmsSenderZhiyan2Impl.class);
    
    private static ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
    }
    
    private ZhiyanOptions zhiyanOptions;
    
    private RestTemplate restTemplate;
    
    private HttpHeaders headers;
    
    public SmsSenderZhiyan2Impl(ZhiyanOptions zhiyanOptions) {
        this.zhiyanOptions = zhiyanOptions;
        this.restTemplate = new RestTemplateBuilder().enableSsl(443).build();
        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", "application/json;charset=UTF-8");
        this.headers.add("Accept-Charset", "UTF-8");
        this.headers.add("Accept", "application/json");
    }
    
    @Listenable
    @Override
    public void send(SmsMessage smsMessage) {
        doSend(smsMessage, zhiyanOptions);
    }
    
    @Override
    public void send(AdvancedSmsMessage smsMsg) {
        if (smsMsg.getOptions() == null) {
            doSend(new SmsMessage(smsMsg.getCellphone(), smsMsg.getMessage()),
                   zhiyanOptions);
            return;
        }
        
        doSend(new SmsMessage(smsMsg.getCellphone(), smsMsg.getMessage()),
               (ZhiyanOptions) smsMsg.getOptions());
    }
    
    private void doSend(SmsMessage smsMessage, ZhiyanOptions _zhiyanOptions) {
        String cellphone = smsMessage.getCellphone();
        String message = smsMessage.getMessage();
        // only support send sms in china
        if (cellphone.startsWith("+") && !cellphone.startsWith("+86")) {
            throw new IllegalArgumentException(String.format("%s is not supported.",
                                                             cellphone));
        }
        if (cellphone.startsWith("+86")) {
            cellphone = cellphone.substring(3);
        }
        
        String uid = UUID.randomUUID().toString().replace("-", "");
        
        Map map = new HashMap();
        map.put("apiKey", _zhiyanOptions.getApiKey());
        map.put("appId", _zhiyanOptions.getToken());
        map.put("templateid", _zhiyanOptions.getTemplateId());
        map.put("mobile", cellphone);
        map.put("param", message);
        map.put("uid", uid);
        map.put("extend", "");
        
        try {
            logger.debug(String.format("Sending sms [to:%s , passcode:%s]",
                                       cellphone,
                                       message));
            long timestamp = System.currentTimeMillis();
            
            String bodyInStr = objectMapper.writeValueAsString(map);
            
            HttpEntity<String> requestEntity = new HttpEntity(bodyInStr,
                                                              headers);
                                                              
            ResponseEntity<String> responseEntity = restTemplate.exchange(_zhiyanOptions.getUrl(),
                                                                          HttpMethod.POST,
                                                                          requestEntity,
                                                                          String.class);
            String response = responseEntity.getBody();
            logger.debug(String.format("Sending sms result: %s", response));
            HttpStatus status = responseEntity.getStatusCode();
            if (status.value() >= 400) {
                throw new RuntimeException(status.getReasonPhrase());
            }
        }
        catch (Throwable e) {
            logger.error("Sending sms failed", e);
        }
    }
    
}
