package in.clouthink.daas.edm.push.impl;

import com.google.gson.JsonObject;
import in.clouthink.daas.edm.Listenable;
import in.clouthink.daas.edm.push.PushMessage;
import in.clouthink.daas.edm.push.PushSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.Map;

public class PushSenderJPushImpl implements PushSender {
    
    private static final Log logger = LogFactory.getLog(PushSenderJPushImpl.class);
    
    private JPushOptions jPushOptions;
    
    private PushClient pushClient;
    
    public PushSenderJPushImpl(JPushOptions jPushOptions) {
        this.jPushOptions = jPushOptions;
        this.pushClient = new PushClient(jPushOptions.getAppSecret(),
                                         jPushOptions.getAppKey(),
                                         jPushOptions.getMaxRetries());
    }
    
    @Listenable
    @Override
    public void push(PushMessage message) {
        if (message.getPushType() == null) {
            throw new IllegalArgumentException("The push type is required.");
        }
        
        switch (message.getPushType()) {
            case All: {
                pushToAll(message);
                return;
            }
            case Group: {
                pushToGroup(message);
                return;
            }
            case Device: {
                pushToDevice(message);
            }
        }
        
        return;
    }
    
    private void pushToAll(PushMessage message) {
        PushPayload payload = createPayloadBuilder(message).setAudience(Audience.all())
                                                           .build();
        push(payload);
    }
    
    private void pushToGroup(PushMessage message) {
        PushPayload payload = createPayloadBuilder(message).setAudience(Audience.tag_and(message.getGroups()))
                                                           .build();
        push(payload);
    }
    
    private void pushToDevice(PushMessage message) {
        PushPayload payload = createPayloadBuilder(message).setAudience(Audience.alias(message.getDevices()))
                                                           .build();
        push(payload);
    }
    
    private PushPayload.Builder createPayloadBuilder(PushMessage message) {
        IosNotification.Builder iosBuilder = IosNotification.newBuilder()
                                                            .setAlert(message.getTitle())
                                                            .incrBadge(1);
        for (Map.Entry<String, Object> entry : message.getAttributes()
                                                      .entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Number) {
                iosBuilder.addExtra(key, (Number) value);
            }
            else if (value instanceof Boolean) {
                iosBuilder.addExtra(key, (Boolean) value);
            }
            else if (value instanceof JsonObject) {
                iosBuilder.addExtra(key, (JsonObject) value);
            }
            else if (value instanceof String) {
                iosBuilder.addExtra(key, (String) value);
            }
            else {
                iosBuilder.addExtra(key, value.toString());
            }
        }
        IosNotification iosNotification = iosBuilder.addExtra("content",
                                                              message.getContent())
                                                    .build();
                                                    
        AndroidNotification.Builder androidBuilder = AndroidNotification.newBuilder()
                                                                        .setAlert(message.getTitle());
                                                                        
        for (Map.Entry<String, Object> entry : message.getAttributes()
                                                      .entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Number) {
                androidBuilder.addExtra(key, (Number) value);
            }
            else if (value instanceof Boolean) {
                androidBuilder.addExtra(key, (Boolean) value);
            }
            else if (value instanceof JsonObject) {
                androidBuilder.addExtra(key, (JsonObject) value);
            }
            else if (value instanceof String) {
                androidBuilder.addExtra(key, (String) value);
            }
            else {
                androidBuilder.addExtra(key, value.toString());
            }
        }
        
        AndroidNotification androidNotification = androidBuilder.addExtra("content",
                                                                          message.getContent())
                                                                .build();
                                                                
        return PushPayload.newBuilder()
                          .setPlatform(Platform.all())
                          .setNotification(Notification.newBuilder()
                                                       .addPlatformNotification(iosNotification)
                                                       .addPlatformNotification(androidNotification)
                                                       .build())
                          .setOptions(Options.newBuilder()
                                             .setTimeToLive(jPushOptions.getTimeToLive())
                                             .setApnsProduction(jPushOptions.isApnsProduction())
                                             .build());
    }
    
    private void push(PushPayload payload) {
        try {
            PushResult result = pushClient.sendPush(payload);
            if (logger.isDebugEnabled()) {
                logger.debug("Message sent, " + payload);
                logger.debug("Got result, " + result);
            }
        }
        catch (APIConnectionException e) {
            logger.error(e, e);
        }
        catch (APIRequestException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("HTTP Status: " + e.getStatus());
                logger.warn("Error Code: " + e.getErrorCode());
                logger.warn("Error Message ", e);
            }
        }
    }
    
}
