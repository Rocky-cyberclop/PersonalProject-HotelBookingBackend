package com.rocky.identityservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    private ObjectId _id;

    private String comment;

    private LocalDate date;

    @Positive(message = "This field need a positive number")
    @NotBlank(message = "This field need a positive number")
    private Integer score;

}
