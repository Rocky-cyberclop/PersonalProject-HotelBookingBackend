package com.rocky.identityservice.dtos;

import com.rocky.identityservice.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private List<Customer> users;
    private int totalPage;
}
