package com.rocky.reservationservice.services.impl;

import com.rocky.reservationservice.dtos.DoneChooseRoomRequest;
import com.rocky.reservationservice.dtos.ReservationWrapper;
import com.rocky.reservationservice.dtos.RoomState;
import com.rocky.reservationservice.dtos.RoomWrapper;
import com.rocky.reservationservice.enums.RoomChosen;
import com.rocky.reservationservice.feigns.IdentityFeign;
import com.rocky.reservationservice.feigns.RoomFeign;
import com.rocky.reservationservice.kafka.ReservationProducerService;
import com.rocky.reservationservice.models.Guest;
import com.rocky.reservationservice.models.Payment;
import com.rocky.reservationservice.models.Reservation;
import com.rocky.reservationservice.repositories.ReservationRepository;
import com.rocky.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
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
        List<Reservation> reservations = reservationRepository.findReservationByDateGoGreaterThanEqualAndGuestTokenNot(come, guest);
        for (Reservation reservation : reservations) {
            if ((go.isAfter(reservation.getDateCome()) || go.isEqual(reservation.getDateCome())) && (reservation.getRooms() != null)) {
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
            reservation.setTotalDate((int) ChronoUnit.DAYS.between(reservation.getDateCome(),
                    reservation.getDateGo()));
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
                        roomState.setFrom(reservation.getDateCome().atTime(LocalTime.of(17, 0))
                                .atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
                        roomState.setTo(reservation.getDateGo().atTime(LocalTime.of(17, 0))
                                .atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));
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
    public String handleDoneChoosingRoom(DoneChooseRoomRequest doneChooseRoomRequest) {
        if (reservationRepository.findReservationByGuestToken(doneChooseRoomRequest.getGuest()) != null
                && !reservationRepository.findReservationByGuestToken(doneChooseRoomRequest.getGuest()).isEmpty()) {
            Reservation reservation = reservationRepository
                    .findReservationByGuestToken(doneChooseRoomRequest.getGuest()).get(0);
            reservation.setGuestToken(null);
            reservation.setGuests(new ArrayList<>());
            for (int i = 0; i < doneChooseRoomRequest.getNumberOfPeople(); i++) {
                Guest guest = new Guest();
                guest.setName("");
                guest.setEmail("");
                guest.setPhone("");
                reservation.getGuests().add(guest);
            }
            reservationRepository.save(reservation);
            return reservation.get_id();
        }
        return null;
    }

    @Override
    public Map<String, String> getInfoForPayment(String id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) return null;
        Map<String, String> result = new HashMap<>();
        result.put("come", reservation.get().getDateCome().toString());
        result.put("go", reservation.get().getDateGo().toString());
        result.put("totalDays", reservation.get().getTotalDate().toString());
        int numberOfGuest = 0;
        for (Guest guest : reservation.get().getGuests()) {
            numberOfGuest++;
        }
        result.put("id", id);
        result.put("adults", Integer.toString(numberOfGuest));
        StringBuilder rooms = new StringBuilder("[");
        Long price = 0L;
        for (RoomWrapper roomWrapper : reservation.get().getRooms()) {
            price += roomWrapper.getPrice();
            rooms.append(roomWrapper.getNumber().toString()).append(", ");
        }
        rooms.append("]");
        result.put("rooms", rooms.toString());
        price *= reservation.get().getTotalDate();
        result.put("price", price.toString());
        result.put("tax", String.valueOf(price * 0.1));
        result.put("total", String.valueOf(price + (price * 0.1)));
        if (reservation.get().getPayment() != null) {
            result.put("paid", "true");
        } else
            result.put("paid", "false");
        return result;
    }

    @Override
    public String bindGuest(String id, List<Guest> guests) {
        Reservation reservation;
        if (reservationRepository.findById(id).isPresent()) {
            reservation = reservationRepository.findById(id).get();
            reservation.setGuests(new ArrayList<>());
            for (Guest guest : guests) {
                reservation.getGuests().add(guest);
            }
            reservation.setCustomerEmail(guests.get(0).getEmail());
            reservationProducerService.sendMail(id, guests.get(0).getEmail());
            reservationRepository.save(reservation);
            return id;
        }
        return null;
    }

    @Override
    public List<Guest> getGuests(String id) {
        List<Guest> guests = new ArrayList<>();
        List<Guest> guestsInReservation;
        if (reservationRepository.findById(id).isPresent()) {
            guestsInReservation = reservationRepository.findById(id).get().getGuests();
            guests.addAll(guestsInReservation);
        }
        return guests;
    }

    @Override
    public List<ReservationWrapper> getReservationWithEmail(String email) {
        List<ReservationWrapper> reservationWrappers = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findReservationByCustomerEmail(email);
        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                ReservationWrapper reservationWrapper = new ReservationWrapper();
                reservationWrapper.setId(reservation.get_id());
                reservationWrapper.setFrom(reservation.getDateCome().toString());
                reservationWrapper.setTo(reservation.getDateGo().toString());
                reservationWrapper.setTotalDays(reservation.getTotalDate());
                reservationWrappers.add(reservationWrapper);
            }
        }
        return reservationWrappers;
    }

    @Override
    public void updatePayment(String id, String paymentId, String total) {
        if (reservationRepository.findById(id).isPresent()) {
            Reservation reservation = reservationRepository.findById(id).get();
            Payment payment = new Payment();
            payment.setCode(paymentId);
            payment.setDate(LocalDate.now());
            payment.setAmount((long) ((Double.parseDouble(total))));
            reservation.setPayment(payment);
            reservationRepository.save(reservation);
        }

    }


}
