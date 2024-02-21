package com.rocky.notificationservice.controllers;

import com.rocky.notificationservice.dtos.RoomState;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChoosingRoomController {
    @MessageMapping("/roomReserve")
    @SendTo("/topic/room-state")
    public RoomState sendMessage(@Payload RoomState roomState) {
        System.out.println(roomState);
        return roomState;
    }
}
