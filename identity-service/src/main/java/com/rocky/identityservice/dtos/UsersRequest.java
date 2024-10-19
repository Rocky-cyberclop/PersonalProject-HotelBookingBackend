package com.rocky.identityservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequest {
    private String text;
    private int page;
    private int size;
}
