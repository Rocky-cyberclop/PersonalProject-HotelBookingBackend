package com.rocky.reservationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationsRequest {
    private String from;
    private String to;
    private int page;
    private int size;
}
