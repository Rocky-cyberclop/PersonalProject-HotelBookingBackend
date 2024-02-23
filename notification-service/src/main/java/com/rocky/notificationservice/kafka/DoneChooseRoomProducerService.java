package com.rocky.notificationservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoneChooseRoomProducerService {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void doneChooseRoom(String guest){
        kafkaTemplate.send("doneChooseRoomTopic", guest);
    }
}
