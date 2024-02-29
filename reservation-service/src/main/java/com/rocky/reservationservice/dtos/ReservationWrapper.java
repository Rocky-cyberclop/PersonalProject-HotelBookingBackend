package com.rocky.reservationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationWrapper {
    private String id;
    private String from;
    private String to;
    private Integer totalDays;
}
