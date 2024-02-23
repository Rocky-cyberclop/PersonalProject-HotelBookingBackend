package com.rocky.reservationservice.controllers;

import com.rocky.reservationservice.dtos.BookedRoomsRequest;
import com.rocky.reservationservice.dtos.ChooseRoomRequest;
import com.rocky.reservationservice.dtos.RoomState;
import com.rocky.reservationservice.kafka.ReservationProducerService;
import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationProducerService reservationProducerService;

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
    public ResponseEntity<String> doneChooseRooms(@RequestBody String guest) {
        return new ResponseEntity<>(reservationService.findByGuest(guest), HttpStatus.OK);
    }
}
