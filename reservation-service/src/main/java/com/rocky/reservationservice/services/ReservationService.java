package com.rocky.reservationservice.services;

import com.rocky.reservationservice.dtos.*;
import com.rocky.reservationservice.models.Guest;
import com.rocky.reservationservice.models.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReservationService {
    String chooseRoom(String from, String to);

    void doClean();

    Set<Integer> getRoomsBooked(String guest, String from, String to);

    void bindRoomToReservation(RoomState roomState);

    void doneChooseRoom(String guest);

    String handleDoneChoosingRoom(DoneChooseRoomRequest doneChooseRoomRequest);

    Map<String, String> getInfoForPayment(String id);

    String bindGuest(String id, List<Guest> guests);

    List<Guest> getGuests(String id);

    List<ReservationWrapper> getReservationWithEmail(String email);

    void updatePayment(String id, String paymentId, String total);

    SuggestRoomsResponse findRoomsFitRequest(ChooseRoomOldConceptRequest chooseRoomRequest);

    ReservationsResponse findAllReservation(ReservationsRequest request);

    Reservation findOne(String id);
}
