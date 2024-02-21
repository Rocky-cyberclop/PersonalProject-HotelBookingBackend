package com.rocky.notificationservice.kafka;

import com.rocky.notificationservice.dtos.RoomState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomStateConsumerService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(groupId = "notificationGroup", topics = "roomTopic")
    public void listenSeatTopic(RoomState roomState) {
        System.out.println(roomState);
        simpMessagingTemplate.convertAndSend("/topic/room-state", roomState);
    }
}
