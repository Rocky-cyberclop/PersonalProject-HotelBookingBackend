package com.rocky.reservationservice.kafka;

import com.rocky.reservationservice.dtos.RoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationProducerService {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void setRoomState(RoomState roomState){
        kafkaTemplate.send("roomTopic", roomState);
    }

    public void doCLean(){
        kafkaTemplate.send("cleanTopic", 1);
    }
}
