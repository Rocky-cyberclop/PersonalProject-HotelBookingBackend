package com.rocky.identityservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IdentityProducerService {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public String doCLean(){
        kafkaTemplate.send("cleanForgetCodeTopic", 1);
        return "Done";
    }

    public void sendMail(String email, String code){
        Map<String, String> toGuest = new HashMap<>();
        toGuest.put("email", email);
        toGuest.put("code", code);
        kafkaTemplate.send("sendMailForgetTopic", toGuest);
    }
}
