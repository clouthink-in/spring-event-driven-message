package in.clouthink.daas.edm.push;

import in.clouthink.daas.edm.Edms;
import in.clouthink.daas.edm.push.impl.JPushOptions;
import in.clouthink.daas.edm.push.impl.PushSenderJPushImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Import this configuration to spring boot application to make jpush auto
 * enabled.
 * 
 * @author dz
 * @since 1.1.3
 */
@Configuration
@EnableConfigurationProperties(JPushOptions.class)
public class PushModuleConfiguration {
    
    @Bean
    @Autowired
    public PushSender pushSenderJPushImpl(JPushOptions jPushOptions) {
        PushSender result = new PushSenderJPushImpl(jPushOptions);
        Edms.getEdm("push").register("push", result);
        return result;
    }
}
