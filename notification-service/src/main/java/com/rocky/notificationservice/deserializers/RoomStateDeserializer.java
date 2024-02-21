package com.rocky.notificationservice.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocky.notificationservice.dtos.RoomState;
import com.rocky.notificationservice.enums.RoomChosen;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class RoomStateDeserializer implements Deserializer<RoomState> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public RoomState deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            // Deserialize JSON bytes into RoomState object
            RoomState roomState = objectMapper.readValue(data, RoomState.class);
            // Deserialize RoomChosen enum separately since it's not directly supported by Jackson
            if (roomState.getType() != null) {
                roomState.setType(RoomChosen.valueOf(roomState.getType().name()));
            }
            return roomState;
        } catch (IOException e) {
            // Handle deserialization exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}
