package com.rocky.reservationservice.kafka;

import com.rocky.reservationservice.dtos.RoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public void sendMail(String id, String email){
        Map<String, String> toGuest = new HashMap<>();
        toGuest.put("id", id);
        toGuest.put("email", email);
        kafkaTemplate.send("sendMailTopic", toGuest);
    }
}
