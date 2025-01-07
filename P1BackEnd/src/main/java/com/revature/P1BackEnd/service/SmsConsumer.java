package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.model.Employee;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class SmsConsumer {

    private final Logger logger = LoggerFactory.getLogger(SmsConsumer.class);
    @Value("${twilio.accountSid}")
    private String ACCOUNT_SID;
    @Value("${twilio.authToken}")
    private String AUTH_TOKEN;
    @Value("${twilio.phoneNumber}")
    private PhoneNumber senderNumber;

    @RabbitListener(queues = "smsQueue")
    public void sendSMS(Message message) {
        try {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            PhoneNumber receiverNumber = new PhoneNumber(message.getMessageProperties().getHeaders().get("phone-number").toString());
            logger.info("Sending SMS: " + messageBody + " \nPhone number: " + receiverNumber);

//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//            com.twilio.rest.api.v2010.account.Message sms = com.twilio.rest.api.v2010.account.Message.creator(
//                    receiverNumber, senderNumber, messageBody).create();

//            logger.info("SMS sent successfully: " + sms.getSid());
        } catch (Exception e) {
            logger.error("Error while sending SMS: {}", e.getMessage(), e);
        }
    }
}
