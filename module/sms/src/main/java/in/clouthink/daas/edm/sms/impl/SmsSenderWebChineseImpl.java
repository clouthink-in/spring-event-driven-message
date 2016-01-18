package in.clouthink.daas.edm.sms.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import in.clouthink.daas.edm.sms.AdvancedSmsMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.clouthink.daas.edm.Listenable;
import in.clouthink.daas.edm.sms.SmsMessage;
import in.clouthink.daas.edm.sms.SmsSender;

public class SmsSenderWebChineseImpl implements SmsSender {
    
    private static final Log logger = LogFactory.getLog(SmsSenderWebChineseImpl.class);
    
    private static final String HEX_CHARS = "0123456789abcdef";
    
    private static MessageDigest md5Digest;
    
    private static ObjectMapper objectMapper;
    
    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
        }
        objectMapper = new ObjectMapper();
    }
    
    private static String getRequestParamString(Map<String, String> requestParam) {
        StringBuffer sb = new StringBuffer("");
        String result = "";
        if (null != requestParam && 0 != requestParam.size()) {
            for (Map.Entry<String, String> en : requestParam.entrySet()) {
                try {
                    sb.append(en.getKey() + "="
                              + (null == en.getValue()
                                 || "".equals(en.getValue()) ? ""
                                                             : URLEncoder.encode(en.getValue(),
                                                                                 "UTF-8"))
                              + "&");
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return "";
                }
            }
            result = sb.substring(0, sb.length() - 1);
        }
        return result;
    }
    
    public Map buildParams(String mobile, String code) {
        Map params = new HashMap();
        try {
            Map map = new HashMap();
            map.put("mobile", mobile);
            map.put("param", code);
            map.put("extend", "");
            params.put("data", objectMapper.writeValueAsString(map));
        }
        catch (JsonProcessingException e) {
            logger.error(e, e);
        }
        return params;
    }
    
    /**
     * 用户数据的MD5签名
     */
    private String signByMd5(String apiKey,
                             String token,
                             String templateId,
                             String cellphone,
                             String passcode,
                             long timestamp) {
        // 注：sign的值为md5(appkey+token+templateId+mobile+param+extend+timestamp)并确保同一个请求在24小时之内，不能够做两次发送，否则返回其签名失败
        StringBuilder sb = new StringBuilder();
        sb.append(apiKey)
          .append(token)
          .append(templateId)
          .append(cellphone)
          .append(passcode)
          .append(timestamp);
        try {
            byte[] resultBytes = md5Digest.digest(sb.toString().getBytes());
            
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < resultBytes.length; i++) {
                result.append(HEX_CHARS.charAt(resultBytes[i] >>> 4 & 0x0F));
                result.append(HEX_CHARS.charAt(resultBytes[i] & 0x0F));
            }
            return result.toString();
        }
        catch (Exception e) {
            logger.error(e, e);
        }
        return "";
    }
    
    private WebChineseOptions webChineseOptions;
    
    private RestTemplate restTemplate;
    
    private HttpHeaders headers;
    
    public SmsSenderWebChineseImpl(WebChineseOptions webChineseOptions) {
        this.webChineseOptions = webChineseOptions;
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.add("Content-Type",
                         "application/x-www-form-urlencoded;charset=utf8");
        this.headers.add("Accept", "*/*");
    }
    
    @Listenable
    @Override
    public void send(SmsMessage smsMessage) {
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
        
        try {
            logger.debug(String.format("Sending sms [to:%s , body:%s]",
                                       cellphone,
                                       message));
            MultiValueMap<String, String> map = new LinkedMultiValueMap();
            map.add("Uid", webChineseOptions.getUid());
            map.add("Key", webChineseOptions.getKey());
            map.add("smsMob", cellphone);
            map.add("smsText", message);
            HttpEntity<String> requestEntity = new HttpEntity(map, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(webChineseOptions.getServer(),
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
    
    @Override
    public void send(AdvancedSmsMessage smsMsg) {
        logger.warn("Send AdvancedSmsMessage is not supported, the options from AdvancedSmsMessage will be ignored, and dispatch the normal SmsMessage Sender");
        send(new SmsMessage(smsMsg.getCellphone(), smsMsg.getMessage()));
    }
    
}
