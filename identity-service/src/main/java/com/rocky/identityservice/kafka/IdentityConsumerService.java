package com.rocky.identityservice.kafka;

import com.rocky.identityservice.services.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IdentityConsumerService {
    @Autowired
    private IdentityService identityService;

    @KafkaListener(topics = "sendMailTopic", groupId = "identityGroup")
    public void consumeDoneChooseRoom(Map<String, String> toGuest){
        identityService.sendMailCompleteReserve(toGuest);
    }
}
