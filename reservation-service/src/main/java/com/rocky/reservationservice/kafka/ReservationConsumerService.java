package com.rocky.reservationservice.kafka;

import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReservationConsumerService {

    @Autowired
    private ReservationService reservationService;

    @KafkaListener(topics = "cleanTopic", groupId = "roomGroup")
    public void consumeClean(Integer isClean){
        reservationService.doClean();
    }

    @KafkaListener(topics = "doneChooseRoomTopic", groupId = "roomGroup")
    public void consumeDoneChooseRoom(String guest){
        reservationService.doneChooseRoom(guest);
    }

}
