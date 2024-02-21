package com.rocky.reservationservice.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("IDENTITY-SERVICE")
public interface IdentityFeign {
    @GetMapping("api/auth/generateToken")
    public ResponseEntity<String> generateRandomToken();
}
