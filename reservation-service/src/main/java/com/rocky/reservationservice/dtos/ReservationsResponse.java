package com.rocky.reservationservice.dtos;

import com.rocky.reservationservice.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationsResponse {
    private List<Reservation> reservations;
    private int totalPage;
}
