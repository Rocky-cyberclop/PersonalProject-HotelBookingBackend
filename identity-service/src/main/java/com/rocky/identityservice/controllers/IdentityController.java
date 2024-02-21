package com.rocky.identityservice.controllers;

import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import com.rocky.identityservice.services.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class IdentityController {

    @Autowired
    private IdentityService identityService;

    @PostMapping("register")
    public ResponseEntity<RegisterRequest> register(@RequestBody RegisterRequest registerRequest){
        return identityService.register(registerRequest);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        if(identityService.login(loginRequest).equals("No email found"))
            return new ResponseEntity<>("No email found", HttpStatus.UNAUTHORIZED);
        if(identityService.login(loginRequest).equals("Pass not match"))
            return new ResponseEntity<>("Pass not match", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(identityService.login(loginRequest), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("generateToken")
    public ResponseEntity<String> generateRandomToken(){
        return new ResponseEntity<>(identityService.generateRandomToken(), HttpStatus.OK);
    }

    @GetMapping("hi")
    public ResponseEntity<String> sayHi(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
}
