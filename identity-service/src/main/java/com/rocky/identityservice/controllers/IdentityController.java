package com.rocky.identityservice.controllers;

import com.rocky.identityservice.dtos.*;
import com.rocky.identityservice.feigns.ReservationFeign;
import com.rocky.identityservice.kafka.IdentityProducerService;
import com.rocky.identityservice.models.Customer;
import com.rocky.identityservice.services.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class IdentityController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ReservationFeign reservationFeign;

    @Autowired
    private IdentityProducerService identityProducerService;

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

    @GetMapping("forget/sendCode/{email}")
    public ResponseEntity<String> sendCodeForget(@PathVariable String email) {
        return new ResponseEntity<>(identityService.sendMaillForgetPass(email), HttpStatus.OK);
    }

    @GetMapping("forget/doClean")
    public ResponseEntity<String> cleanForgetCode() {
        return new ResponseEntity<>(identityProducerService.doCLean(), HttpStatus.OK);
    }

    @PostMapping("forget/login")
    public ResponseEntity<String> loginWithCode(@RequestBody Map<String, String> request) {
        return new ResponseEntity<>(identityService.loginWithCode(
                request.get("email"), request.get("code")), HttpStatus.OK);
    }

    @PostMapping("reset")
    public ResponseEntity<String> resetPass(@RequestHeader("loggedEmail") String email,
                                            @RequestBody Map<String, String> request) {
        return new ResponseEntity<>(identityService.resetPassword(email, request.get("password"),
                request.get("newPassword")), HttpStatus.OK);
    }

    @GetMapping("comments/{page}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable Integer page) {
        return new ResponseEntity<>(identityService.getComment(page), HttpStatus.OK);
    }

    @PostMapping("postComments")
    public ResponseEntity<String> postComment(@RequestHeader("loggedEmail") String email,
                                              @RequestBody Map<String, String> content) throws UnsupportedEncodingException {
        return new ResponseEntity<>(identityService.postComment(
                email, content.get("content")), HttpStatus.OK);
    }
}
