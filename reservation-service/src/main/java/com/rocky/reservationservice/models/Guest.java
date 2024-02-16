package com.rocky.reservationservice.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    @Id
    private String _id;
    @NotBlank(message = "Name can not be blank")
    private String name;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email can not be blank")
    private String email;
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Phone number is not valid")
    @NotBlank(message = "Phone can not be blank")
    private String phone;
}
