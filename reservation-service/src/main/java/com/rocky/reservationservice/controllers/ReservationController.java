package com.rocky.reservationservice.controllers;

import com.rocky.reservationservice.dtos.RoomState;
import com.rocky.reservationservice.kafka.RoomStateProducerService;
import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomStateProducerService roomStateProducerService;

    @GetMapping("hi")
    public ResponseEntity<String> sayHi() {
        return new ResponseEntity<>("Hi from reservation service!", HttpStatus.OK);
    }

    @GetMapping("chooseRoom")
    public ResponseEntity<String> chooseRoom() {
        return new ResponseEntity<>(reservationService.chooseRoom(), HttpStatus.OK);
    }

    @PostMapping("reserve")
    public ResponseEntity<String> reserve(@RequestBody RoomState roomState) {
        roomStateProducerService.setRoomState(roomState);
        return new ResponseEntity<>("Reserved", HttpStatus.OK);
    }
}
