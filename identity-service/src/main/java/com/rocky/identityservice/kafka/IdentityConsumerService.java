package com.rocky.identityservice.kafka;

import com.rocky.identityservice.services.EmailService;
import com.rocky.identityservice.services.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IdentityConsumerService {
    @Autowired
    private IdentityService identityService;

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "sendMailTopic", groupId = "identityGroup")
    public void consumeDoneChooseRoom(Map<String, String> toGuest) {
        identityService.sendMailCompleteReserve(toGuest);
    }

    @KafkaListener(topics = "sendMailForgetTopic", groupId = "identityGroup")
    public void consumeSendMailForget(Map<String, String> customer) {
        String subject = "This is your login code:" + customer.get("code");
        subject += "\nThis code will expire in 5 minutes!";
        emailService.sendEmail(customer.get("email"), "Login code", subject);
    }

    @KafkaListener(topics = "cleanForgetCodeTopic", groupId = "identityGroup")
    public void consumeCleanForgetCodeClean(Integer a) {
        identityService.cleanForgetCode();
    }
}
