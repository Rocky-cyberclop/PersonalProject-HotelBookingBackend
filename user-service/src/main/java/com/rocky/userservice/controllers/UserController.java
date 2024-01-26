package com.rocky.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    @GetMapping("hi")
    public ResponseEntity<String> sayHi(@RequestHeader("loggedEmail") String email){
        return new ResponseEntity<>(
                "Hello from user entity and email logged is: "+email, HttpStatus.OK);
    }
}
