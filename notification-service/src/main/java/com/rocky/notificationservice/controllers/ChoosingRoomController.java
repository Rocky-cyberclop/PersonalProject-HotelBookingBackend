package com.rocky.notificationservice.controllers;

import com.rocky.notificationservice.dtos.RoomState;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChoosingRoomController {

    @MessageMapping("/connect")
    @SendTo("/topic/room-state")
    public RoomState addClient(@Payload RoomState roomState, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        Objects.requireNonNull(simpMessageHeaderAccessor.getSessionAttributes()).put("guest", roomState.getGuest());
        return roomState;
    }

    @MessageMapping("/roomReserve")
    @SendTo("/topic/room-state")
    public RoomState sendMessage(@Payload RoomState roomState) {
        return roomState;
    }
}
