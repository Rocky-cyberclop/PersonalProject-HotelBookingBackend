package com.rocky.reservationservice.controllers;

import com.rocky.reservationservice.dtos.*;
import com.rocky.reservationservice.kafka.ReservationProducerService;
import com.rocky.reservationservice.models.Guest;
import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationProducerService reservationProducerService;

    @GetMapping("{id}")
    public ResponseEntity<Map<String, String>> getInfoForPayment(@PathVariable String id){
        return new ResponseEntity<>(reservationService.getInfoForPayment(id),HttpStatus.OK);
    }

    @GetMapping("guests/{id}")
    public ResponseEntity<List<Guest>> bindGuest(@PathVariable String id){
        return new ResponseEntity<>(reservationService.getGuests(id),HttpStatus.OK);
    }

    @PostMapping("{id}")
    public ResponseEntity<String> bindGuests(@PathVariable String id, @RequestBody List<Guest> guests){
        return new ResponseEntity<>(reservationService.bindGuest(id, guests),HttpStatus.OK);
    }

    @GetMapping("doClean")
    public ResponseEntity<String> doCleanReservation() {
        reservationProducerService.doCLean();
        return new ResponseEntity<>("Done!", HttpStatus.OK);
    }

    @PostMapping("floor")
    public ResponseEntity<Set<Integer>> getRoomBooked(@RequestBody BookedRoomsRequest bookedRoomsRequest) {
        return new ResponseEntity<>(reservationService.getRoomsBooked(bookedRoomsRequest.getGuest()
                , bookedRoomsRequest.getFrom(), bookedRoomsRequest.getTo()), HttpStatus.OK);
    }

    @PostMapping("chooseRoom")
    public ResponseEntity<String> chooseRoom(@RequestBody ChooseRoomRequest chooseRoomRequest) {
        return new ResponseEntity<>(reservationService.chooseRoom(chooseRoomRequest.getFrom(), chooseRoomRequest.getTo()), HttpStatus.OK);
    }

    @PostMapping("reserve")
    public ResponseEntity<String> reserve(@RequestBody RoomState roomState) {
        reservationProducerService.setRoomState(roomState);
        reservationService.bindRoomToReservation(roomState);
        return new ResponseEntity<>("Reserved", HttpStatus.OK);
    }

    @PostMapping("doneChooseRoom")
    public ResponseEntity<String> doneChooseRooms(@RequestBody DoneChooseRoomRequest doneChooseRoomRequest) {
        return new ResponseEntity<>(reservationService.handleDoneChoosingRoom(doneChooseRoomRequest), HttpStatus.OK);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<List<ReservationWrapper>> getReservationWithEmail(@PathVariable String email){
        return new ResponseEntity<>(reservationService.getReservationWithEmail(email),HttpStatus.OK);
    };
}
