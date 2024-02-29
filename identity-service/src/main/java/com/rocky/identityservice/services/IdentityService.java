package com.rocky.identityservice.services;

import com.rocky.identityservice.dtos.CustomerDto;
import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import com.rocky.identityservice.models.Customer;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IdentityService {
    ResponseEntity<RegisterRequest> register(RegisterRequest registerRequest);

    String login(LoginRequest loginRequest);

    String generateRandomToken();

    void sendMailCompleteReserve(Map<String, String> toGuest);

    Map<String, String> getName(String email);

    CustomerDto getInfo(String email);

    String updateInfo(CustomerDto customerDto);
}
