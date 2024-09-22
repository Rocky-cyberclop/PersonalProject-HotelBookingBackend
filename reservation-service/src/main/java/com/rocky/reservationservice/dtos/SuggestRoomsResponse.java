package com.rocky.reservationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestRoomsResponse implements Serializable {
    private List<RoomWrapper> rooms;
    private Long total;
}
