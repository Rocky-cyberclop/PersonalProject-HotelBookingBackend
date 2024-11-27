package com.rocky.reservationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseRoomOldConceptRequest {
    private String from;
    private String to;
    private Integer page;
    private Integer[] filter;
    private Integer adults;
    private Integer children;
    private Integer numberOfRoom;
    private Integer price;
    private Integer[] selectedRooms;
}
