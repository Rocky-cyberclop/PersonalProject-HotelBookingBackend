package com.rocky.reservationservice.controllers;

import com.rocky.reservationservice.dtos.*;
import com.rocky.reservationservice.kafka.ReservationProducerService;
import com.rocky.reservationservice.models.Guest;
import com.rocky.reservationservice.services.ReservationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {

    private final String BASE_API_PAYPAL = "https://api-m.sandbox.paypal.com";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationProducerService reservationProducerService;

    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public String generateAccessToken() {
        String auth = this.getAuth(
                "AWJN8uzl55bfKeR4Q2fjVDrpTnArGbmRoIqa-6fHRY-0QHzAfviY0c9lgbuDtogtUI__jdvvZ14Ptf-t",
                "EDDwjY186tVFSezBeAXHLD9AOUqFs--9MSlVETxHeVR9if6ckv80a0irzqc1WB8wBvCqGU0Wr_jVaYpt"
        );
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE_API_PAYPAL + "/v1/oauth2/token",
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, String>> getInfoForPayment(@PathVariable String id) {
        return new ResponseEntity<>(reservationService.getInfoForPayment(id), HttpStatus.OK);
    }

    @GetMapping("guests/{id}")
    public ResponseEntity<List<Guest>> bindGuest(@PathVariable String id) {
        return new ResponseEntity<>(reservationService.getGuests(id), HttpStatus.OK);
    }

    @PostMapping("{id}")
    public ResponseEntity<String> bindGuests(@PathVariable String id, @RequestBody List<Guest> guests) {
        return new ResponseEntity<>(reservationService.bindGuest(id, guests), HttpStatus.OK);
    }

    @GetMapping("doClean")
    public ResponseEntity<String> doCleanReservation() {
        reservationProducerService.doCLean();
        return new ResponseEntity<>("Done!", HttpStatus.OK);
    }

    @PostMapping("floor")
    public ResponseEntity<Set<Integer>> getRoomBooked(@RequestBody BookedRoomsRequest bookedRoomsRequest) {
        return new ResponseEntity<>(reservationService.getRoomsBooked(bookedRoomsRequest.getGuest()
                , bookedRoomsRequest.getFrom(), bookedRoomsRequest.getTo()), HttpStatus.OK);
    }

    @PostMapping("chooseRoom")
    public ResponseEntity<String> chooseRoom(@RequestBody ChooseRoomRequest chooseRoomRequest) {
        return new ResponseEntity<>(reservationService.chooseRoom(chooseRoomRequest.getFrom(), chooseRoomRequest.getTo()), HttpStatus.OK);
    }

    @PostMapping("reserve")
    public ResponseEntity<String> reserve(@RequestBody RoomState roomState) {
        reservationProducerService.setRoomState(roomState);
        reservationService.bindRoomToReservation(roomState);
        return new ResponseEntity<>("Reserved", HttpStatus.OK);
    }

    @PostMapping("doneChooseRoom")
    public ResponseEntity<String> doneChooseRooms(@RequestBody DoneChooseRoomRequest doneChooseRoomRequest) {
        return new ResponseEntity<>(reservationService.handleDoneChoosingRoom(doneChooseRoomRequest), HttpStatus.OK);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<List<ReservationWrapper>> getReservationWithEmail(@PathVariable String email) {
        return new ResponseEntity<>(reservationService.getReservationWithEmail(email), HttpStatus.OK);
    }

    @PostMapping("create-paypal-order")
    public ResponseEntity<Object> createPayment(@RequestBody Map<String, String> orderRequest) {

        String accessToken = generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //JSON String
        String requestJson = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\"USD\",\"value\":\"" + orderRequest.get("total") + "\"}}]}";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE_API_PAYPAL + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } else {
            System.out.println("Unavailable to get CAPTURE ORDER, STATUS CODE " + response.getStatusCode());
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("capture-paypal-order")
    public ResponseEntity<Object> capturePayment(@RequestBody Map<String, String> paymentRequest) {

        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE_API_PAYPAL + "/v2/checkout/orders/" + paymentRequest.get("paymentId") + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            reservationService.updatePayment(paymentRequest.get("id"), paymentRequest.get("paymentId"), paymentRequest.get("total"));
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } else {
            System.out.println("Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode());
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
}
