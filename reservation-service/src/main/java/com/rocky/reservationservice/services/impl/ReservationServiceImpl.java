package com.rocky.reservationservice.services.impl;

import com.rocky.reservationservice.feigns.IdentityFeign;
import com.rocky.reservationservice.repositories.ReservationRepository;
import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private IdentityFeign identityFeign;
    @Override
    public String chooseRoom() {
        return identityFeign.generateRandomToken().getBody();
    }
}
