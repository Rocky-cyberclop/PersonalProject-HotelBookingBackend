package com.rocky.reservationservice.services.impl;

import com.rocky.reservationservice.dtos.RoomState;
import com.rocky.reservationservice.dtos.RoomWrapper;
import com.rocky.reservationservice.enums.RoomChosen;
import com.rocky.reservationservice.feigns.IdentityFeign;
import com.rocky.reservationservice.feigns.RoomFeign;
import com.rocky.reservationservice.kafka.ReservationProducerService;
import com.rocky.reservationservice.models.Payment;
import com.rocky.reservationservice.models.Reservation;
import com.rocky.reservationservice.repositories.ReservationRepository;
import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private IdentityFeign identityFeign;
    @Autowired
    private RoomFeign roomFeign;
    @Autowired
    private ReservationProducerService reservationProducerService;

    @Override
    public String chooseRoom(String from, String to) {
        LocalDate come = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        LocalDate leave = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        String guestToken = identityFeign.generateRandomToken().getBody();
        Reservation reservation = new Reservation();
        reservation.setGuestToken(guestToken);
        reservation.setDateCome(come);
        reservation.setDateGo(leave);
        reservation.setTotalDate((int) ChronoUnit.DAYS.between(come, leave));
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setCustomerEmail("chossing@gmail.com");
        reservationRepository.save(reservation);
        return guestToken;
    }

    @Override
    public void doClean() {
        List<Reservation> reservations = reservationRepository.findReservationByPayment(null);
        List<Reservation> reservationsToRemove = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerEmail() == null) {
                reservationsToRemove.add(reservation);
            } else {
                Duration duration = Duration.between(reservation.getCreatedAt(), LocalDateTime.now());
                if (duration.toHours() > 1) {
                    reservationsToRemove.add(reservation);
                }
            }
        }
        reservationRepository.deleteAll(reservationsToRemove);
    }

    @Override
    public Set<Integer> getRoomsBooked(String guest, String from, String to) {
        LocalDate come = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        LocalDate go = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        Set<Integer> roomsBooked = new HashSet<>();
        List<Reservation> reservations = reservationRepository.findReservationByDateGoGreaterThanAndGuestTokenNot(come, guest);
        for (Reservation reservation : reservations) {
            if (go.isAfter(reservation.getDateCome()) && (reservation.getRooms() != null)) {
                for (RoomWrapper roomWrapper : reservation.getRooms()) {
                    roomsBooked.add(roomWrapper.getNumber());
                }
            }
        }
        return roomsBooked;
    }

    @Override
    public void bindRoomToReservation(RoomState roomState) {
        Reservation reservation;
        if (!reservationRepository.findReservationByGuestToken(roomState.getGuest()).isEmpty()) {
            reservation = reservationRepository.findReservationByGuestToken(roomState.getGuest()).get(0);
        } else {
            reservation = new Reservation();
            reservation.setDateCome(LocalDate.parse(roomState.getFrom(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
            reservation.setDateGo(LocalDate.parse(roomState.getTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
            reservation.setCustomerEmail("chossing@gmail.com");
            reservation.setGuestToken(roomState.getGuest());
            reservation.setCreatedAt(LocalDateTime.now());
        }
        RoomWrapper roomWrapperFromFeign = Objects.requireNonNull(roomFeign.getRoom(roomState.getRoom()).getBody());
        if (roomState.getType().equals(RoomChosen.RESERVE)) {
            if (reservation.getRooms() == null) reservation.setRooms(new ArrayList<>());
            reservation.getRooms().add(roomWrapperFromFeign);
        } else {
            if (reservation.getRooms() == null) reservation.setRooms(new ArrayList<>());
            reservation.getRooms().remove(roomWrapperFromFeign);
        }
        reservationRepository.save(reservation);

    }

    @Override
    public void doneChooseRoom(String guest) {
        if (!reservationRepository.findReservationByGuestToken(guest).isEmpty()) {
            Reservation reservation = reservationRepository.findReservationByGuestToken(guest).get(0);
            if (reservation.getCustomerEmail().equals("chossing@gmail.com")) {
                if (reservation.getRooms() != null && !reservation.getRooms().isEmpty()) {
                    for (RoomWrapper roomWrapper : reservation.getRooms()) {
                        RoomState roomState = new RoomState();
                        roomState.setFrom(reservation.getDateCome().toString());
                        roomState.setTo(reservation.getDateGo().toString());
                        roomState.setGuest("notanyguest");
                        roomState.setRoom(roomWrapper.getNumber());
                        roomState.setType(RoomChosen.UNRESERVED);
                        reservationProducerService.setRoomState(roomState);
                    }
                }
            }
            reservationRepository.delete(reservation);
        }
    }

    @Override
    public String findByGuest(String guest) {
        if (reservationRepository.findReservationByGuestToken(guest) != null
                && !reservationRepository.findReservationByGuestToken(guest).isEmpty()){
            Reservation reservation = reservationRepository.findReservationByGuestToken(guest).get(0);
            reservation.setGuestToken(null);
            reservationRepository.save(reservation);
            return reservation.get_id();
        }
        return null;
    }


}
