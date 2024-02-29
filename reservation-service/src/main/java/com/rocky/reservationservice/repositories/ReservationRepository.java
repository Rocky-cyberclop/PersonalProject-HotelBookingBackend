package com.rocky.reservationservice.repositories;

import com.rocky.reservationservice.models.Payment;
import com.rocky.reservationservice.models.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findReservationByPayment(Payment payment);

    List<Reservation> findReservationByDateGoGreaterThanEqualAndGuestTokenNot(LocalDate from, String guest);

    List<Reservation> findReservationByGuestToken(String guest);

    List<Reservation> findReservationByCustomerEmail(String email);

}
