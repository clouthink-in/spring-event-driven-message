# Introduction

The lightweight event driven message framework based on reactor.

# Dependencies

* Reactor 2.0.4.RELEASE

# Usage

So far the following version is available 

module name | latest version
------|------
daas-edm|1.0.2
daas-edm-email|1.0.0
daas-edm-push|1.1.2
daas-edm-sms|1.2.1

## Maven

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-edm</artifactId>
        <version>${daas.edm.version}</version>
    </dependency>

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-edm-email</artifactId>
        <version>${daas.edm.email.version}</version>
    </dependency>

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-edm-push</artifactId>
        <version>${daas.edm.push.version}</version>
    </dependency>

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-edm-sms</artifactId>
        <version>${daas.edm.sms.version}</version>
    </dependency>

## Gradle

    compile "in.clouthink.daas:daas-edm:${daas_edm_version}"


## Spring Configuration

    @Value("${mail.smtp.host}")
    private String smtpHost;
    
    @Value("${mail.smtp.port}")
    private int smtpPort;
    
    @Value("${mail.smtp.username}")
    private String smtpUsername;
    
    @Value("${mail.smtp.password}")
    private String smtpPassword;
    
    @Value("${sms.server.host}")
    private String smsServer;
    
    @Value("${sms.account.uid}")
    private String smsUid;
    
    @Value("${sms.account.key}")
    private String smsKey;
    
    @Value("${push.appKey}")
    private String pushAppKey;
    
    @Value("${push.appSecret}")
    private String pushAppSecret;
    
    @Value("${push.maxRetries}")
    private int pushMaxRetries;
    
    @Value("${push.timeToLive}")
    private long pushTimeToLive;
    
    @Value("${push.iosProduction}")
    private boolean pushIosProduction;

    private boolean initialized = false;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl result = new JavaMailSenderImpl();
        result.setHost(smtpHost);
        result.setPort(smtpPort);
        result.setUsername(smtpUsername);
        result.setPassword(smtpPassword);
        result.getJavaMailProperties().put("mail.smtp.auth", true);
        result.getJavaMailProperties().put("mail.smtp.starttls.enable", true);
        return result;
    }
    
    @Bean
    public SmsSender smsSender() {
        WebChineseOptions options = new WebChineseOptions();
        options.setKey(smsKey);
        options.setServer(smsServer);
        options.setUid(smsUid);
        return new SmsSenderWebChineseImpl(options);
    }
    
    @Bean
    public EmailSender emailSender() {
        return new EmailSenderImpl(javaMailSender());
    }
    
    @Bean
    public PushSender pushSender() {
        JPushOptions options = new JPushOptions();
        options.setApnsProduction(pushIosProduction);
        options.setAppKey(pushAppKey);
        options.setAppSecret(pushAppSecret);
        options.setMaxRetries(pushMaxRetries);
        options.setTimeToLive(pushTimeToLive);
        return new PushSenderJPushImpl(options);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!initialized) {
            Edms.getEdm().register("email", emailSender());
            Edms.getEdm().register("sms", smsSender());
            Edms.getEdm().register("push", pushSender());
            initialized = true;
        }
    }
    
