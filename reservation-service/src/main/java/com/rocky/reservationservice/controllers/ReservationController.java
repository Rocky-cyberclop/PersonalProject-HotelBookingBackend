package com.rocky.reservationservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {
    @GetMapping("hi")
    public ResponseEntity<String> sayHi(){
        return new ResponseEntity<>("Hi from reservation service!", HttpStatus.OK);
    }
}
