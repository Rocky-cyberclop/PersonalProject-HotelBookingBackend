package com.rocky.identityservice.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class RegisterRequest {

    @NotBlank(message = "This field can not be blank")
    private String name;

    private String email;

    private String phone;

    @NotBlank(message = "This field can not be blank")
    private String password;

    private LocalDate dateOfBirth;

    private boolean gender;

    private String address;

    private String nationality;
}
