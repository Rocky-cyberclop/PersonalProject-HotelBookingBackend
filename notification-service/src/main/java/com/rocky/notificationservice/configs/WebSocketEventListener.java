package com.rocky.notificationservice.configs;

import com.rocky.notificationservice.kafka.DoneChooseRoomProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    @Autowired
    private DoneChooseRoomProducerService doneChooseRoomProducerService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String guest = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("guest");
        doneChooseRoomProducerService.doneChooseRoom(guest);
    }
}
