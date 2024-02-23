package com.rocky.reservationservice.services;

import com.rocky.reservationservice.dtos.RoomState;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface ReservationService {
    String chooseRoom(String from, String to);

    void doClean();

    Set<Integer> getRoomsBooked(String guest, String from, String to);

    void bindRoomToReservation(RoomState roomState);

    void doneChooseRoom(String guest);

    String findByGuest(String guest);
}
