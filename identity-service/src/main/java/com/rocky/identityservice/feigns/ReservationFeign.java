package com.rocky.identityservice.feigns;

import com.rocky.identityservice.dtos.ReservationWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("RESERVATION-SERVICE")
public interface ReservationFeign {
    @GetMapping("api/reservation/email/{email}")
    public ResponseEntity<List<ReservationWrapper>> getReservationWithEmail(@PathVariable String email);
}
