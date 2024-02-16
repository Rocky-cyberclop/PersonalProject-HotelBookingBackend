package com.rocky.reservationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document("reservation")
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    private String _id;
    private LocalDate dateCome;
    private LocalDate dateGo;
    private Integer totalDate;
    private String guestToken;
    private List<Guest> guests;
    private Payment payment;
}
