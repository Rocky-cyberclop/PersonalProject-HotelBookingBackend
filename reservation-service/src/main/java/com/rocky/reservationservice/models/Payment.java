package com.rocky.reservationservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    private String _id;
    @NotBlank(message = "Amount can not be blank")
    @Positive(message = "Amount can not be negative")
    private Long amount;
    private LocalDate date;
    private String code;
}
