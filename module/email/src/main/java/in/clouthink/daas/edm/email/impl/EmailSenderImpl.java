package in.clouthink.daas.edm.email.impl;

import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import in.clouthink.daas.edm.Listenable;
import in.clouthink.daas.edm.email.EmailMessage;
import in.clouthink.daas.edm.email.EmailSender;

public class EmailSenderImpl implements EmailSender {
    
    private JavaMailSender mailSender;
    
    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Listenable
    @Override
    public void send(final EmailMessage emailMessage) {
        mailSender.send(new MimeMessagePreparator() {
            
            @Override
            public void prepare(MimeMessage message) throws Exception {
                String from = emailMessage.getFrom();
                String sender = emailMessage.getSender();
                String to = emailMessage.getTo();
                String subject = emailMessage.getSubject();
                String body = emailMessage.getMessage();
                
                InternetAddress fromAddress = new InternetAddress(from, sender);
                message.setRecipient(javax.mail.Message.RecipientType.TO,
                                     new InternetAddress(to));
                message.setFrom(fromAddress);
                message.setReplyTo(new Address[] { fromAddress });
                message.setSubject(subject);
                message.setSentDate(new Date());
                message.setDataHandler(new DataHandler(body,
                                                       "text/html; charset=UTF-8"));
            }
        });
    }
}
