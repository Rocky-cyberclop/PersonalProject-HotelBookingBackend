package com.rocky.reservationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomWrapper implements Serializable {
    private String title;
    private Integer number;
    private List<String> images;
    private Long price;
    private String description;
    private Boolean booked;
    private Integer capacity;
}
