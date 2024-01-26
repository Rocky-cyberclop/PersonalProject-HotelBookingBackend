package com.rocky.identityservice.services;

import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IdentityService {
    ResponseEntity<RegisterRequest> register(RegisterRequest registerRequest);

    String login(LoginRequest loginRequest);
}
