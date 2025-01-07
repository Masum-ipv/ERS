package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailConsumer {
    private final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);
    private final JavaMailSender javaMailSender;

    public EmailConsumer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = "emailQueue")
    public void send(Message message) {

        try {
            String emailBody = new String(message.getBody(), StandardCharsets.UTF_8);
            String emailAddress = (String) message.getMessageProperties().getHeaders().get("email-address");
            logger.info("Sending email: " + emailBody + " from email address: " + emailAddress);

//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setTo(emailAddress);
//            mailMessage.setSubject("Don't forget to pray 5 times a day");
//            mailMessage.setText(emailBody);
//            javaMailSender.send(mailMessage);
//
//            logger.info("Email sent successfully");
        } catch (Exception e) {
            logger.error("Error while sending email: " + e.getMessage());
        }
    }
}
