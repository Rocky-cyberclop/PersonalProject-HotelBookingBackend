package com.rocky.identityservice.controllers;

import com.rocky.identityservice.dtos.CustomerDto;
import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import com.rocky.identityservice.dtos.ReservationWrapper;
import com.rocky.identityservice.feigns.ReservationFeign;
import com.rocky.identityservice.models.Customer;
import com.rocky.identityservice.services.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class IdentityController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ReservationFeign reservationFeign;

    @PostMapping("register")
    public ResponseEntity<RegisterRequest> register(@RequestBody RegisterRequest registerRequest) {
        return identityService.register(registerRequest);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        if (identityService.login(loginRequest).equals("No email found"))
            return new ResponseEntity<>("No email found", HttpStatus.UNAUTHORIZED);
        if (identityService.login(loginRequest).equals("Pass not match"))
            return new ResponseEntity<>("Pass not match", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(identityService.login(loginRequest), HttpStatus.OK);
    }

    @GetMapping("generateToken")
    public ResponseEntity<String> generateRandomToken() {
        return new ResponseEntity<>(identityService.generateRandomToken(), HttpStatus.OK);
    }

    @GetMapping("email")
    public ResponseEntity<Map<String, String>> getName(@RequestHeader("loggedEmail") String email) {
        return new ResponseEntity<>(identityService.getName(email), HttpStatus.OK);
    }

    @GetMapping("info")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader("loggedEmail") String email) {
        return new ResponseEntity<>(identityService.getInfo(email), HttpStatus.OK);
    }

    @PutMapping("info")
    public ResponseEntity<String> updateInfo(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(identityService.updateInfo(customerDto), HttpStatus.OK);
    }

    @GetMapping("reservation")
    public ResponseEntity<List<ReservationWrapper>> getReservation(@RequestHeader("loggedEmail") String email) {
        return reservationFeign.getReservationWithEmail(email);
    }
}
